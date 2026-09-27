package com.example.cappucare.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey val id: String,
    val nombre: String,
    val descripcion: String,
    val categoria: Categoria,
    val cantidadDisponible: Int,
    val unidadMedida: String,
    val stockMinimo: Int,
    val ubicacion: String,
    val estado: String = "Disponible"
) {
    val isStockBajo: Boolean
        get() = cantidadDisponible <= stockMinimo
}
