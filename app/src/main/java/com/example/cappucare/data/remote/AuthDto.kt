package com.example.cappucare.data.remote

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("token") val token: String?,
    @SerializedName("username") val username: String,
    @SerializedName("nombreCompleto") val nombreCompleto: String?,
    @SerializedName("rol") val rol: String
)
