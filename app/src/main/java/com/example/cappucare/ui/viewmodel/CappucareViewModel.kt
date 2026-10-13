package com.example.cappucare.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cappucare.data.local.AppDatabase
import com.example.cappucare.data.model.Categoria
import com.example.cappucare.data.model.Movimiento
import com.example.cappucare.data.model.Producto
import com.example.cappucare.data.model.TipoMovimiento
import com.example.cappucare.data.repository.CappucareRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CappucareViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CappucareRepository

    val allProductos: StateFlow<List<Producto>>
    val productosStockBajo: StateFlow<List<Producto>>
    val allMovimientos: StateFlow<List<Movimiento>>

    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow<Categoria?>(null)

    val filteredProductos: StateFlow<List<Producto>>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = CappucareRepository(database.productoDao(), database.movimientoDao())

        allProductos = repository.allProductos.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        productosStockBajo = repository.productosStockBajo.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allMovimientos = repository.allMovimientos.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        filteredProductos = combine(allProductos, searchQuery, selectedCategory) { list, query, category ->
            list.filter { product ->
                val matchesQuery = query.isEmpty() ||
                        product.nombre.contains(query, ignoreCase = true) ||
                        product.id.contains(query, ignoreCase = true) ||
                        product.ubicacion.contains(query, ignoreCase = true)
                val matchesCategory = category == null || product.categoria == category
                matchesQuery && matchesCategory
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    suspend fun getProductoById(id: String): Producto? {
        return repository.getProductoById(id)
    }

    fun getMovimientosByProducto(productoId: String): Flow<List<Movimiento>> {
        return repository.getMovimientosByProducto(productoId)
    }

    fun guardarProducto(
        id: String,
        nombre: String,
        descripcion: String,
        categoria: Categoria,
        cantidadDisponible: Int,
        unidadMedida: String,
        stockMinimo: Int,
        ubicacion: String,
        estado: String = "Disponible",
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val producto = Producto(
                id = id.trim(),
                nombre = nombre.trim(),
                descripcion = descripcion.trim(),
                categoria = categoria,
                cantidadDisponible = cantidadDisponible,
                unidadMedida = unidadMedida.trim(),
                stockMinimo = stockMinimo,
                ubicacion = ubicacion.trim(),
                estado = estado
            )
            repository.insertOrUpdateProducto(producto)
            onSuccess()
        }
    }

    fun registrarMovimiento(
        productoId: String,
        productoNombre: String,
        tipo: TipoMovimiento,
        cantidad: Int,
        motivo: String,
        usuarioResponsable: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val movimiento = Movimiento(
                productoId = productoId,
                productoNombre = productoNombre,
                tipo = tipo,
                cantidad = cantidad,
                motivo = motivo.trim(),
                fechaHora = System.currentTimeMillis(),
                usuarioResponsable = usuarioResponsable.trim()
            )

            val exito = repository.registrarMovimiento(movimiento)
            if (exito) {
                onSuccess()
            } else {
                onError("No hay suficiente stock disponible para realizar la salida.")
            }
        }
    }

    fun eliminarProducto(producto: Producto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteProducto(producto)
            onSuccess()
        }
    }
}