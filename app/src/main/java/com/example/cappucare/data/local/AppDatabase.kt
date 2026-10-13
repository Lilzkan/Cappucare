package com.example.cappucare.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cappucare.data.mock.MockData
import com.example.cappucare.data.model.Movimiento
import com.example.cappucare.data.model.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Producto::class, Movimiento::class], version = 1, exportSchema = false)
@TypeConverters(ConvertidoresRoom::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun movimientoDao(): MovimientoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cappucare_database"
                )
                    .addCallback(AppDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val context: Context
        ) : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database)
                    }
                }
            }

            suspend fun populateDatabase(db: AppDatabase) {
                val productoDao = db.productoDao()
                val movimientoDao = db.movimientoDao()

                MockData.sampleProducts.forEach { product ->
                    if (productoDao.getProductoById(product.id) == null) {
                        productoDao.insertProducto(product)
                    }
                }

                MockData.sampleMovements.forEach { movement ->
                    movimientoDao.insertMovimiento(movement)
                }
            }
        }
    }
}