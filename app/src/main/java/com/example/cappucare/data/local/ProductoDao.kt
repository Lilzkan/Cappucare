package com.example.cappucare.data.local

import androidx.room.*
import com.example.cappucare.data.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun obtenerTodosLosProductos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun obtenerProductoPorId(id: String): Producto?

    @Query("SELECT * FROM productos WHERE cantidadDisponible <= stockMinimo ORDER BY cantidadDisponible ASC")
    fun obtenerProductosStockBajo(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE cantidadDisponible <= stockMinimo ORDER BY cantidadDisponible ASC")
    suspend fun obtenerProductosStockBajoDirecto(): List<Producto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(producto: Producto)

    @Update
    suspend fun actualizarProducto(producto: Producto)

    @Delete
    suspend fun eliminarProducto(producto: Producto)
}