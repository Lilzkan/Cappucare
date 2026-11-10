package com.example.cappucare.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClienteClimaRetrofit {
    private const val URL_BASE_CLIMA = "https://api.open-meteo.com/"

    val servicioClimaApi: ClimaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE_CLIMA)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClimaApiService::class.java)
    }
}
