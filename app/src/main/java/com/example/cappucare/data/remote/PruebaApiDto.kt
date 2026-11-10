package com.example.cappucare.data.remote

import com.google.gson.annotations.SerializedName

data class PruebaApiDto(
    @SerializedName("userId") val usuarioId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val titulo: String,
    @SerializedName("completed") val completado: Boolean
)
