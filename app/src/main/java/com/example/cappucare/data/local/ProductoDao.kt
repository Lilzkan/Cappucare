package com.example.cappucare.data.local

import androidx.room.*
import com.example.cappucare.data.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun getAllProductos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun getProductoById(id: String): Producto?

    @Query("SELECT * FROM productos WHERE cantidadDisponible <= stockMinimo ORDER BY cantidadDisponible ASC")
    fun getProductosStockBajo(): Flow<List<Producto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducto(producto: Producto)

    @Update
    suspend fun updateProducto(producto: Producto)

    @Delete
    suspend fun deleteProducto(producto: Producto)

    @Query("INSERT INTO productos (id, nombre, descripcion, categoria, cantidadDisponible, unidadMedida, stockMinimo, ubicacion, estado) VALUES (:id, :nombre, :descripcion, :categoria, :cantidadDisponible, :unidadMedida, :stockMinimo, :ubicacion, :estado)")
    suspend fun insertRaw(
        id: String,
        nombre: String,
        descripcion: String,
        categoria: String,
        cantidadDisponible: Int,
        unidadMedida: String,
        stockMinimo: Int,
        ubicacion: String,
        estado: String
    )
}