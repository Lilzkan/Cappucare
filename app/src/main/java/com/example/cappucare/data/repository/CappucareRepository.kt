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
    val allProductos: Flow<List<Producto>> = productoDao.getAllProductos()
    val productosStockBajo: Flow<List<Producto>> = productoDao.getProductosStockBajo()
    val allMovimientos: Flow<List<Movimiento>> = movimientoDao.getAllMovimientos()

    suspend fun getProductoById(id: String): Producto? {
        return productoDao.getProductoById(id)
    }

    fun getMovimientosByProducto(productoId: String): Flow<List<Movimiento>> {
        return movimientoDao.getMovimientosByProducto(productoId)
    }

    suspend fun insertOrUpdateProducto(producto: Producto) {
        productoDao.insertProducto(producto)
    }

    suspend fun deleteProducto(producto: Producto) {
        productoDao.deleteProducto(producto)
    }

    suspend fun registrarMovimiento(movimiento: Movimiento): Boolean {
        val producto = productoDao.getProductoById(movimiento.productoId) ?: return false

        val nuevaCantidad = if (movimiento.tipo == TipoMovimiento.INGRESO) {
            producto.cantidadDisponible + movimiento.cantidad
        } else {
            if (producto.cantidadDisponible < movimiento.cantidad) return false
            producto.cantidadDisponible - movimiento.cantidad
        }

        val productoActualizado = producto.copy(cantidadDisponible = nuevaCantidad)
        productoDao.updateProducto(productoActualizado)
        movimientoDao.insertMovimiento(movimiento)
        return true
    }
}