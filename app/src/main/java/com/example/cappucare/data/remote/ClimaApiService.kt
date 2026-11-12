package com.example.cappucare.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ClimaApiService {
    @GET("v1/forecast")
    suspend fun obtenerClima(
        @Query("latitude") latitud: Double = -33.4489,
        @Query("longitude") longitud: Double = -70.6693,
        @Query("current_weather") climaActual: Boolean = true,
        @Query("timezone") zonaHoraria: String = "auto"
    ): RespuestaClimaDto
}
