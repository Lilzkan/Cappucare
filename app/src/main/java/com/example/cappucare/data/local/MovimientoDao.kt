package com.example.cappucare.data.local

import androidx.room.*
import com.example.cappucare.data.model.Movimiento
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoDao {
    @Query("SELECT * FROM movimientos ORDER BY fechaHora DESC")
    fun obtenerTodosLosMovimientos(): Flow<List<Movimiento>>

    @Query("SELECT * FROM movimientos WHERE productoId = :productoId ORDER BY fechaHora DESC")
    fun obtenerMovimientosPorProducto(productoId: String): Flow<List<Movimiento>>

    @Query("SELECT * FROM movimientos WHERE sincronizado = 0")
    suspend fun obtenerMovimientosNoSincronizados(): List<Movimiento>

    @Query("UPDATE movimientos SET sincronizado = 1 WHERE id = :id")
    suspend fun marcarComoSincronizado(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMovimiento(movimiento: Movimiento)
}