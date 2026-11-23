package com.example.cappucare.data.repository

import android.content.Context
import com.example.cappucare.data.local.MovimientoDao
import com.example.cappucare.data.local.ProductoDao
import com.example.cappucare.data.model.Movimiento
import com.example.cappucare.data.model.Producto
import com.example.cappucare.data.model.TipoMovimiento
import com.example.cappucare.data.remote.ClienteClimaRetrofit
import com.example.cappucare.data.remote.ClienteRetrofit
import com.example.cappucare.data.remote.LoginRequest
import com.example.cappucare.data.remote.LoginResponse
import com.example.cappucare.ui.utils.ClimaHelper
import com.example.cappucare.ui.utils.InfoClimaDia
import com.example.cappucare.ui.utils.NotificacionHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class CappucareRepository(
    private val productoDao: ProductoDao,
    private val movimientoDao: MovimientoDao
) {
    private val mutexRed = Mutex()
    val todosLosProductos: Flow<List<Producto>> = productoDao.obtenerTodosLosProductos()
    val productosStockBajo: Flow<List<Producto>> = productoDao.obtenerProductosStockBajo()
    val todosLosMovimientos: Flow<List<Movimiento>> = movimientoDao.obtenerTodosLosMovimientos()

    suspend fun obtenerProductoPorId(id: String): Producto? = withContext(Dispatchers.IO) {
        productoDao.obtenerProductoPorId(id)
    }

    fun obtenerMovimientosPorProducto(productoId: String): Flow<List<Movimiento>> {
        return movimientoDao.obtenerMovimientosPorProducto(productoId)
    }

    suspend fun insertarOActualizarProducto(producto: Producto) = withContext(Dispatchers.IO) {
        productoDao.insertarProducto(producto)
    }

    suspend fun eliminarProducto(producto: Producto) = withContext(Dispatchers.IO) {
        productoDao.eliminarProducto(producto)
    }

    suspend fun registrarMovimiento(movimiento: Movimiento): Result<Unit> = withContext(Dispatchers.IO) {
        val producto = productoDao.obtenerProductoPorId(movimiento.productoId)
            ?: return@withContext Result.failure(Exception("El insumo no fue encontrado en el inventario."))

        val esSalida = movimiento.tipo == TipoMovimiento.SALIDA

        if (esSalida) {
            if (producto.cantidadDisponible < movimiento.cantidad) {
                return@withContext Result.failure(Exception("La cantidad solicitada excede el stock disponible (${producto.cantidadDisponible})."))
            }

            try {
                val response = ClienteRetrofit.servicioApi.enviarMovimientoRemote(movimiento)
                if (response.isSuccessful) {
                    val nuevaCantidad = producto.cantidadDisponible - movimiento.cantidad
                    val productoActualizado = producto.copy(cantidadDisponible = nuevaCantidad, version = producto.version + 1)
                    productoDao.actualizarProducto(productoActualizado)
                    val movimientoFinal = movimiento.copy(sincronizado = true)
                    movimientoDao.insertarMovimiento(movimientoFinal)
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("El servidor rechazó la venta: El stock fue actualizado o consumido por otro dispositivo."))
                }
            } catch (_: Exception) {
                Result.failure(Exception("Requerida conexión a internet para realizar ventas en tiempo real y evitar descuadres de stock."))
            }
        } else {
            val nuevaCantidad = producto.cantidadDisponible + movimiento.cantidad
            val productoActualizado = producto.copy(cantidadDisponible = nuevaCantidad, version = producto.version + 1)
            productoDao.actualizarProducto(productoActualizado)

            var fueEnviado = false
            try {
                val response = ClienteRetrofit.servicioApi.enviarMovimientoRemote(movimiento)
                fueEnviado = response.isSuccessful
            } catch (_: Exception) {
                fueEnviado = false
            }

            val movimientoFinal = movimiento.copy(sincronizado = fueEnviado)
            movimientoDao.insertarMovimiento(movimientoFinal)
            Result.success(Unit)
        }
    }

    suspend fun sincronizarMovimientosPendientes(): Int = withContext(Dispatchers.IO) {
        mutexRed.withLock {
            val pendientes = movimientoDao.obtenerMovimientosNoSincronizados()
            var exitosos = 0
            for (mov in pendientes) {
                try {
                    val resp = ClienteRetrofit.servicioApi.enviarMovimientoRemote(mov)
                    if (resp.isSuccessful) {
                        movimientoDao.marcarComoSincronizado(mov.id)
                        exitosos++
                    }
                } catch (_: Exception) {
                    // Sigue estando offline
                }
            }
            exitosos
        }
    }

    suspend fun descargarProductosDeApi(): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val remotos = ClienteRetrofit.servicioApi.obtenerProductosRemote()
            for (prod in remotos) {
                productoDao.insertarProducto(prod)
            }
            Result.success(remotos.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verificarYNotificarStockBajo(context: Context) = withContext(Dispatchers.IO) {
        val criticos = productoDao.obtenerProductosStockBajoDirecto()
        if (criticos.isNotEmpty()) {
            NotificacionHelper.enviarNotificacionResumenStockBajo(context, criticos)
        }
    }

    suspend fun obtenerCantidadPendientes(): Int = withContext(Dispatchers.IO) {
        movimientoDao.obtenerMovimientosNoSincronizados().size
    }
    suspend fun loginRemote(username: String, pass: String): Result<LoginResponse> = withContext(Dispatchers.IO) {
        try {
            val req = LoginRequest(username, pass)
            val resp = ClienteRetrofit.servicioApi.loginRemote(req)
            if (resp.isSuccessful && resp.body() != null) {
                Result.success(resp.body()!!)
            } else {
                Result.failure(Exception("Credenciales inválidas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerClimaYFecha(): InfoClimaDia = withContext(Dispatchers.IO) {
        val (nombreDia, numeroDia, mesAno) = ClimaHelper.obtenerFechaActualFormateada()
        val (nombreMes, ano) = mesAno
        val momento = ClimaHelper.obtenerMomentoDelDia()

        try {
            val resp = ClienteClimaRetrofit.servicioClimaApi.obtenerClima()
            val (descripcion, icono) = ClimaHelper.traducirCodigoClimaWmo(resp.climaActual.codigoClima, resp.climaActual.esDia)
            InfoClimaDia(
                nombreDia = nombreDia,
                numeroDia = numeroDia,
                nombreMes = nombreMes,
                ano = ano,
                temperatura = "${resp.climaActual.temperatura} °C",
                descripcionClima = descripcion,
                iconoClima = icono,
                momentoDia = momento
            )
        } catch (_: Exception) {
            InfoClimaDia(
                nombreDia = nombreDia,
                numeroDia = numeroDia,
                nombreMes = nombreMes,
                ano = ano,
                temperatura = "-- °C",
                descripcionClima = "Sin conexión",
                iconoClima = "🌤️",
                momentoDia = momento
            )
        }
    }
}
