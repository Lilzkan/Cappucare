package com.example.cappucare.data.remote

import com.example.cappucare.data.model.Movimiento
import com.example.cappucare.data.model.Producto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("todos/{id}")
    suspend fun obtenerPruebaPorId(@Path("id") id: Int): PruebaApiDto

    @GET("productos")
    suspend fun obtenerProductosRemote(): List<Producto>

    @POST("movimientos")
    suspend fun enviarMovimientoRemote(@Body movimiento: Movimiento): Response<Unit>

    @POST("auth/login")
    suspend fun loginRemote(@Body request: LoginRequest): Response<LoginResponse>
}
