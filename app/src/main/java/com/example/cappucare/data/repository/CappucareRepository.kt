package com.example.cappucare.data.repository

import com.example.cappucare.data.local.MovimientoDao
import com.example.cappucare.data.local.ProductoDao
import com.example.cappucare.data.model.Movimiento
import com.example.cappucare.data.model.Producto
import com.example.cappucare.data.model.TipoMovimiento
import kotlinx.coroutines.flow.Flow

class CappucareRepository(
    private val productoDao: ProductoDao,
    private val movimientoDao: MovimientoDao
) {
    val todosLosProductos: Flow<List<Producto>> = productoDao.obtenerTodosLosProductos()
    val productosStockBajo: Flow<List<Producto>> = productoDao.obtenerProductosStockBajo()
    val todosLosMovimientos: Flow<List<Movimiento>> = movimientoDao.obtenerTodosLosMovimientos()

    suspend fun obtenerProductoPorId(id: String): Producto? {
        return productoDao.obtenerProductoPorId(id)
    }

    fun obtenerMovimientosPorProducto(productoId: String): Flow<List<Movimiento>> {
        return movimientoDao.obtenerMovimientosPorProducto(productoId)
    }

    suspend fun insertarOActualizarProducto(producto: Producto) {
        productoDao.insertarProducto(producto)
    }

    suspend fun eliminarProducto(producto: Producto) {
        productoDao.eliminarProducto(producto)
    }

    suspend fun registrarMovimiento(movimiento: Movimiento): Boolean {
        val producto = productoDao.obtenerProductoPorId(movimiento.productoId) ?: return false

        val nuevaCantidad = if (movimiento.tipo == TipoMovimiento.INGRESO) {
            producto.cantidadDisponible + movimiento.cantidad
        } else {
            if (producto.cantidadDisponible < movimiento.cantidad) return false
            producto.cantidadDisponible - movimiento.cantidad
        }

        val productoActualizado = producto.copy(cantidadDisponible = nuevaCantidad)
        productoDao.actualizarProducto(productoActualizado)
        movimientoDao.insertarMovimiento(movimiento)
        return true
    }
}