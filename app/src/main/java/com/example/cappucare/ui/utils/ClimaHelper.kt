package com.example.cappucare.ui.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class InfoClimaDia(
    val nombreDia: String,
    val numeroDia: String,
    val nombreMes: String,
    val ano: String,
    val temperatura: String,
    val descripcionClima: String,
    val iconoClima: String
)

object ClimaHelper {

    fun obtenerFechaActualFormateada(): Triple<String, String, Pair<String, String>> {
        val calendario = Calendar.getInstance()
        val formatoDiaSemana = SimpleDateFormat("EEEE", Locale("es", "ES"))
        val formatoMes = SimpleDateFormat("MMMM", Locale("es", "ES"))

        val nombreDia = formatoDiaSemana.format(calendario.time).replaceFirstChar { it.uppercase() }
        val numeroDia = calendario.get(Calendar.DAY_OF_MONTH).toString()
        val nombreMes = formatoMes.format(calendario.time).replaceFirstChar { it.uppercase() }
        val ano = calendario.get(Calendar.YEAR).toString()

        return Triple(nombreDia, numeroDia, Pair(nombreMes, ano))
    }

    fun traducirCodigoClimaWmo(codigo: Int): Pair<String, String> {
        return when (codigo) {
            0 -> Pair("Despejado / Soleado", "☀️")
            1, 2, 3 -> Pair("Parcialmente Nublado", "⛅")
            45, 48 -> Pair("Niebla", "🌫️")
            51, 53, 55, 56, 57 -> Pair("Llovizna", "🌦️")
            61, 63, 65, 66, 67 -> Pair("Lluvia", "🌧️")
            71, 73, 75, 77 -> Pair("Nieve", "❄️")
            80, 81, 82 -> Pair("Chubascos", "🌧️")
            95, 96, 99 -> Pair("Tormenta Eléctrica", "⛈️")
            else -> Pair("Tiempo Templado", "🌡️")
        }
    }
}
