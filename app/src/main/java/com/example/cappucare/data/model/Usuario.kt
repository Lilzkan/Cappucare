package com.example.cappucare.data.model

enum class RolUsuario(val displayName: String) {
    ADMIN("Administrador"),
    TRABAJADOR("Trabajador")
}

data class Usuario(
    val username: String,
    val nombreCompleto: String,
    val rol: RolUsuario,
    val token: String? = null
)
