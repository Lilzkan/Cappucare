package com.example.cappucare.data.remote

import com.google.gson.annotations.SerializedName

data class RespuestaClimaDto(
    @SerializedName("current_weather") val climaActual: ClimaActualDto
)

data class ClimaActualDto(
    @SerializedName("temperature") val temperatura: Double,
    @SerializedName("weathercode") val codigoClima: Int,
    @SerializedName("time") val tiempoIso: String
)
