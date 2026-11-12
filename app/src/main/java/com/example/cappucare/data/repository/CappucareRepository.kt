package com.example.cappucare.data.repository

import com.example.cappucare.data.local.MovimientoDao
import com.example.cappucare.data.local.ProductoDao
import com.example.cappucare.data.model.Movimiento
import com.example.cappucare.data.model.Producto
import com.example.cappucare.data.model.TipoMovimiento
import com.example.cappucare.data.remote.ClienteClimaRetrofit
import com.example.cappucare.data.remote.ClienteRetrofit
import com.example.cappucare.data.remote.LoginRequest
import com.example.cappucare.data.remote.LoginResponse
import com.example.cappucare.data.remote.PruebaApiDto
import com.example.cappucare.ui.utils.ClimaHelper
import com.example.cappucare.ui.utils.InfoClimaDia
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

    suspend fun registrarMovimiento(movimiento: Movimiento): Boolean = withContext(Dispatchers.IO) {
        val producto = productoDao.obtenerProductoPorId(movimiento.productoId) ?: return@withContext false

        val nuevaCantidad = if (movimiento.tipo == TipoMovimiento.INGRESO) {
            producto.cantidadDisponible + movimiento.cantidad
        } else {
            if (producto.cantidadDisponible < movimiento.cantidad) return@withContext false
            producto.cantidadDisponible - movimiento.cantidad
        }

        val productoActualizado = producto.copy(cantidadDisponible = nuevaCantidad, version = producto.version + 1)
        productoDao.actualizarProducto(productoActualizado)

        // Intentar enviar a la API
        var fueEnviado = false
        try {
            val response = ClienteRetrofit.servicioApi.enviarMovimientoRemote(movimiento)
            fueEnviado = response.isSuccessful
        } catch (e: Exception) {
            fueEnviado = false
        }

        val movimientoFinal = movimiento.copy(sincronizado = fueEnviado)
        movimientoDao.insertarMovimiento(movimientoFinal)
        true
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
                } catch (e: Exception) {
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

    suspend fun obtenerCantidadPendientes(): Int = withContext(Dispatchers.IO) {
        movimientoDao.obtenerMovimientosNoSincronizados().size
    }

    suspend fun probandoRetrofit(id: Int = 1): Result<PruebaApiDto> = withContext(Dispatchers.IO) {
        try {
            val respuesta = ClienteRetrofit.servicioApi.obtenerPruebaPorId(id)
            Result.success(respuesta)
        } catch (e: Exception) {
            Result.failure(e)
        }
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
        } catch (e: Exception) {
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