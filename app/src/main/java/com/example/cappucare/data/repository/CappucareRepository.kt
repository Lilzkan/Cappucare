package com.example.cappucare.data.repository

import com.example.cappucare.data.local.MovimientoDao
import com.example.cappucare.data.local.ProductoDao
import com.example.cappucare.data.model.Movimiento
import com.example.cappucare.data.model.Producto
import com.example.cappucare.data.model.TipoMovimiento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CappucareRepository(
    private val productoDao: ProductoDao,
    private val movimientoDao: MovimientoDao
) {
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

        val productoActualizado = producto.copy(cantidadDisponible = nuevaCantidad)
        productoDao.actualizarProducto(productoActualizado)
        movimientoDao.insertarMovimiento(movimiento)
        true
    }
}