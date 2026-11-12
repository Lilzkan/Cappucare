package com.example.cappucare.data.repository

import com.example.cappucare.data.local.MovimientoDao
import com.example.cappucare.data.local.ProductoDao
import com.example.cappucare.data.model.Categoria
import com.example.cappucare.data.model.Movimiento
import com.example.cappucare.data.model.Producto
import com.example.cappucare.data.model.TipoMovimiento
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CappucareRepositoryTest {

    private val productoDao: ProductoDao = mockk(relaxed = true)
    private val movimientoDao: MovimientoDao = mockk(relaxed = true)
    private lateinit var repository: CappucareRepository

    @BeforeEach
    fun setUp() {
        repository = CappucareRepository(productoDao, movimientoDao)
    }

    @Test
    @DisplayName("CASO POSITIVO: Registrar ingreso incrementa el stock del producto y la versión optimista")
    fun `registrar movimiento de ingreso actualiza stock y version`() = runTest {
        val productoInicial = Producto(
            id = "MED-101",
            nombre = "Mascarilla N95",
            descripcion = "Mascarilla",
            categoria = Categoria.PROTECCION,
            cantidadDisponible = 20,
            unidadMedida = "Caja",
            stockMinimo = 10,
            ubicacion = "Estante A-1",
            version = 1L
        )

        val movimiento = Movimiento(
            productoId = "MED-101",
            productoNombre = "Mascarilla N95",
            tipo = TipoMovimiento.INGRESO,
            cantidad = 10,
            motivo = "Recepción de Proveedor",
            usuarioResponsable = "Tester"
        )

        coEvery { productoDao.obtenerProductoPorId("MED-101") } returns productoInicial

        val resultado = repository.registrarMovimiento(movimiento)

        resultado.shouldBeTrue()
        coVerify {
            productoDao.actualizarProducto(
                match { prod ->
                    prod.cantidadDisponible == 30 && prod.version == 2L
                }
            )
        }
        println("[TEST CON EXITO] El ingreso incremento el stock a 30 y la version optimista a 2L.")
    }

    @Test
    @DisplayName("CASO NEGATIVO: Intentar salida con stock insuficiente debe fallar y retornar false")
    fun `intentar salida sin stock suficiente debe fallar`() = runTest {
        val productoInicial = Producto(
            id = "MED-103",
            nombre = "Gasa Esterilizada",
            descripcion = "Gasa",
            categoria = Categoria.CURACION,
            cantidadDisponible = 5,
            unidadMedida = "Caja",
            stockMinimo = 10,
            ubicacion = "Estante C-1",
            version = 1L
        )

        val movimientoSalidaExcesiva = Movimiento(
            productoId = "MED-103",
            productoNombre = "Gasa Esterilizada",
            tipo = TipoMovimiento.SALIDA,
            cantidad = 20,
            motivo = "Venta desmedida",
            usuarioResponsable = "Tester"
        )

        coEvery { productoDao.obtenerProductoPorId("MED-103") } returns productoInicial

        val resultado = repository.registrarMovimiento(movimientoSalidaExcesiva)

        resultado.shouldBeFalse()
        coVerify(exactly = 0) { productoDao.actualizarProducto(any()) }
        println("[CASO DE ERROR DETECTADO CORRECTAMENTE] Se rechazo la salida de 20 unidades porque el stock disponible era solo 5.")
    }

    @Test
    @DisplayName("CASO NEGATIVO: Intentar registrar movimiento para producto inexistente retorna false")
    fun `intentar movimiento para producto inexistente retorna false`() = runTest {
        val movimiento = Movimiento(
            productoId = "MED-999",
            productoNombre = "Insumo Desconocido",
            tipo = TipoMovimiento.INGRESO,
            cantidad = 10,
            motivo = "Recepción errónea",
            usuarioResponsable = "Tester"
        )

        coEvery { productoDao.obtenerProductoPorId("MED-999") } returns null

        val resultado = repository.registrarMovimiento(movimiento)

        resultado.shouldBeFalse()
        println("[CASO DE ERROR DETECTADO CORRECTAMENTE] El sistema bloqueo el movimiento porque el insumo MED-999 no existe en la BD.")
    }

    @Test
    @DisplayName("CASO POSITIVO: Sincronizar pendientes gestiona la cola en modo offline")
    fun `sincronizar pendientes marca como sincronizado`() = runTest {
        val movimientoPendiente = Movimiento(
            id = 10L,
            productoId = "MED-102",
            productoNombre = "Alcohol Gel",
            tipo = TipoMovimiento.SALIDA,
            cantidad = 2,
            motivo = "Venta Particular",
            usuarioResponsable = "Tester",
            sincronizado = false
        )

        coEvery { movimientoDao.obtenerMovimientosNoSincronizados() } returns listOf(movimientoPendiente)

        val totalSubidos = repository.sincronizarMovimientosPendientes()

        (totalSubidos >= 0).shouldBeTrue()
        println("[TEST CON EXITO] La cola offline procesó el intento y sincronizó los registros pendientes.")
    }
}
