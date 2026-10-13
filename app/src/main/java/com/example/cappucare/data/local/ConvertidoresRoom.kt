package com.example.cappucare.data.local

import androidx.room.TypeConverter
import com.example.cappucare.data.model.Categoria
import com.example.cappucare.data.model.TipoMovimiento

class ConvertidoresRoom {
    @TypeConverter
    fun deCategoria(categoria: Categoria): String {
        return categoria.name
    }

    @TypeConverter
    fun aCategoria(value: String): Categoria {
        return try {
            Categoria.valueOf(value)
        } catch (e: Exception) {
            Categoria.DESCARTABLES
        }
    }

    @TypeConverter
    fun deTipoMovimiento(tipo: TipoMovimiento): String {
        return tipo.name
    }

    @TypeConverter
    fun aTipoMovimiento(value: String): TipoMovimiento {
        return try {
            TipoMovimiento.valueOf(value)
        } catch (e: Exception) {
            TipoMovimiento.INGRESO
        }
    }
}