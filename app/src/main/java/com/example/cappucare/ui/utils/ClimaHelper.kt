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
    val iconoClima: String,
    val momentoDia: String
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

    fun obtenerMomentoDelDia(): String {
        val hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hora) {
            in 6..11 -> "Mañana"
            in 12..18 -> "Tarde"
            else -> "Noche"
        }
    }

    fun traducirCodigoClimaWmo(codigo: Int, esDia: Int?): Pair<String, String> {
        val esDeNoche = esDia == 0
        return when (codigo) {
            0 -> if (esDeNoche) Pair("Noche Despejada", "🌙") else Pair("Día Soleado", "☀️")
            1, 2, 3 -> if (esDeNoche) Pair("Noche Parcialmente Nublada", "🌙☁️") else Pair("Día Parcialmente Nublado", "⛅")
            45, 48 -> Pair("Niebla", "🌫️")
            51, 53, 55, 56, 57 -> Pair("Llovizna", "🌦️")
            61, 63, 65, 66, 67 -> Pair("Lluvia", "🌧️")
            71, 73, 75, 77 -> Pair("Nieve", "❄️")
            80, 81, 82 -> Pair("Chubascos", "🌧️")
            95, 96, 99 -> Pair("Tormenta Eléctrica", "⛈️")
            else -> if (esDeNoche) Pair("Noche Templada", "🌙") else Pair("Día Templado", "🌡️")
        }
    }
}
