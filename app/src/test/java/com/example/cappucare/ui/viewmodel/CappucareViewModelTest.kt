package com.example.cappucare.ui.viewmodel

import com.example.cappucare.data.model.Categoria
import com.example.cappucare.ui.utils.ClimaHelper
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CappucareViewModelTest {

    @Test
    @DisplayName("CASO POSITIVO: Formatos de fecha y clima helper producen traducciones correctas")
    fun `clima helper traduce codigos WMO correctamente`() {
        val (descSoleado, iconoSoleado) = ClimaHelper.traducirCodigoClimaWmo(0)
        descSoleado shouldBe "Despejado / Soleado"
        iconoSoleado shouldBe "☀️"

        val (nombreDia, numeroDia, mesAno) = ClimaHelper.obtenerFechaActualFormateada()
        nombreDia.shouldNotBeNull()
        numeroDia.shouldNotBeNull()
        mesAno.first.shouldNotBeNull()

        println("[TEST CON EXITO] El Helper de Clima tradujo el codigo WMO 0 a 'Despejado / Soleado' ☀️ y formatea la fecha correctamente.")
    }

    @Test
    @DisplayName("CASO CONTROL DE BORDES: Código meteorológico WMO no mapeado usa fallback seguro")
    fun `clima helper maneja codigos WMO desconocidos con fallback`() {
        val (descDesconocido, iconoDesconocido) = ClimaHelper.traducirCodigoClimaWmo(9999)
        descDesconocido shouldBe "Tiempo Templado"
        iconoDesconocido shouldBe "🌡️"

        println("[CASO DE ERROR DETECTADO CORRECTAMENTE] Ante un codigo WMO inexistente (9999), el sistema aplico el fallback seguro 'Tiempo Templado' 🌡️.")
    }

    @Test
    @DisplayName("CASO POSITIVO: Categorías médicas mantienen nombres legibles")
    fun `categorias mantienen display names correctos`() {
        Categoria.PROTECCION.displayName shouldBe "Protección Personal"
        Categoria.DESCARTABLES.displayName shouldBe "Descartables"

        println("[TEST CON EXITO] Los nombres visibles de las categorias medicas corresponden a la interfaz en espanol.")
    }
}
