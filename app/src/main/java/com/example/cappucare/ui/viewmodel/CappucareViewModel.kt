package com.example.cappucare.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cappucare.data.local.AppDatabase
import com.example.cappucare.data.local.SesionManager
import com.example.cappucare.data.model.*
import com.example.cappucare.data.remote.ClienteRetrofit
import com.example.cappucare.data.repository.CappucareRepository
import com.example.cappucare.ui.utils.InfoClimaDia
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CappucareViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CappucareRepository
    val sesionManager: SesionManager = SesionManager(application)

    val usuarioActual = MutableStateFlow<Usuario?>(sesionManager.obtenerUsuarioActual())
    val esAdmin: StateFlow<Boolean> = usuarioActual.map { it?.rol == RolUsuario.ADMIN }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = sesionManager.obtenerUsuarioActual()?.rol == RolUsuario.ADMIN
    )

    val todosLosProductos: StateFlow<List<Producto>>
    val productosStockBajo: StateFlow<List<Producto>>
    val todosLosMovimientos: StateFlow<List<Movimiento>>

    val consultaBusqueda = MutableStateFlow("")
    val categoriaSeleccionada = MutableStateFlow<Categoria?>(null)

    val productosFiltrados: StateFlow<List<Producto>>

    val cantidadPendientes = MutableStateFlow(0)
    val estadoSincronizacion = MutableStateFlow("Listo")

    val infoClimaDia = MutableStateFlow<InfoClimaDia?>(null)

    init {
        val database = AppDatabase.getDatabase(application)
        repository = CappucareRepository(database.productoDao(), database.movimientoDao())

        todosLosProductos = repository.todosLosProductos.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        productosStockBajo = repository.productosStockBajo.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        todosLosMovimientos = repository.todosLosMovimientos.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        productosFiltrados = combine(todosLosProductos, consultaBusqueda, categoriaSeleccionada) { list, query, category ->
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

        actualizarCantidadPendientes()
        cargarClimaActual()

        ClienteRetrofit.tokenJwtSupplier = { sesionManager.obtenerTokenJwt() }

        iniciarAutoRefrescoEnVivo()
    }

    private fun iniciarAutoRefrescoEnVivo() {
        viewModelScope.launch {
            while (isActive) {
                sincronizarConBackend()
                delay(4000)
            }
        }
    }

    fun iniciarSesion(
        username: String,
        pass: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val resRemote = repository.loginRemote(username, pass)
            resRemote.onSuccess { resp ->
                val rol = try {
                    RolUsuario.valueOf(resp.rol.uppercase())
                } catch (_: Exception) {
                    RolUsuario.TRABAJADOR
                }
                val u = Usuario(
                    username = resp.username,
                    nombreCompleto = resp.nombreCompleto ?: resp.username,
                    rol = rol,
                    token = resp.token
                )
                sesionManager.guardarSesion(u)
                usuarioActual.value = u
                onSuccess()
            }.onFailure {
                val fallbackUser = sesionManager.validarLoginOffline(username, pass)
                if (fallbackUser != null) {
                    sesionManager.guardarSesion(fallbackUser)
                    usuarioActual.value = fallbackUser
                    onSuccess()
                } else {
                    onError("Credenciales incorrectas. Revisa tu usuario y contraseña.")
                }
            }
        }
    }

    fun cerrarSesion(onLogout: () -> Unit) {
        sesionManager.cerrarSesion()
        usuarioActual.value = null
        onLogout()
    }

    fun cargarClimaActual() {
        viewModelScope.launch {
            infoClimaDia.value = repository.obtenerClimaYFecha()
        }
    }

    suspend fun obtenerProductoPorId(id: String): Producto? {
        return repository.obtenerProductoPorId(id)
    }

    fun obtenerMovimientosPorProducto(productoId: String): Flow<List<Movimiento>> {
        return repository.obtenerMovimientosPorProducto(productoId)
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
            repository.insertarOActualizarProducto(producto)
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

            val resultado = repository.registrarMovimiento(movimiento)
            actualizarCantidadPendientes()
            resultado.onSuccess {
                onSuccess()
            }.onFailure { err ->
                onError(err.message ?: "No fue posible realizar la operación.")
            }
        }
    }

    fun actualizarCantidadPendientes() {
        viewModelScope.launch {
            cantidadPendientes.value = repository.obtenerCantidadPendientes()
        }
    }

    fun sincronizarConBackend() {
        viewModelScope.launch {
            estadoSincronizacion.value = "Sincronizando..."
            val subidos = repository.sincronizarMovimientosPendientes()
            val resDescarga = repository.descargarProductosDeApi()

            resDescarga.onSuccess { total ->
                estadoSincronizacion.value = "Éxito ($subidos subidos, $total descargados)"
                repository.verificarYNotificarStockBajo(getApplication())
            }.onFailure { err ->
                estadoSincronizacion.value = "Sin conexión: ${err.localizedMessage}"
                repository.verificarYNotificarStockBajo(getApplication())
            }
            actualizarCantidadPendientes()
            cargarClimaActual()
        }
    }

    fun eliminarProducto(producto: Producto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.eliminarProducto(producto)
            onSuccess()
        }
    }
}
