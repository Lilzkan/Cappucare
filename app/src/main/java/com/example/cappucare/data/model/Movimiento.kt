package com.example.cappucare.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movimientos")
data class Movimiento(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productoId: String,
    val productoNombre: String,
    val tipo: TipoMovimiento,
    val cantidad: Int,
    val motivo: String,
    val fechaHora: Long = System.currentTimeMillis(),
    val usuarioResponsable: String
)
