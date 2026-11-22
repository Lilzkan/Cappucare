package com.example.cappucare.ui.utils

data class ResultadoValidacion(
    val esValido: Boolean,
    val errores: Map<String, String> = emptyMap()
)

object ValidadoresHelper {

    fun validarProducto(
        id: String,
        nombre: String,
        cantidadText: String,
        stockMinimoText: String,
        ubicacion: String
    ): ResultadoValidacion {
        val errores = mutableMapOf<String, String>()

        if (id.trim().isEmpty()) {
            errores["id"] = "El código SKU o identificador es obligatorio."
        }

        if (nombre.trim().length < 3) {
            errores["nombre"] = "El nombre del insumo debe tener al menos 3 caracteres."
        }

        val cantidad = cantidadText.toIntOrNull()
        if (cantidad == null || cantidad < 0) {
            errores["cantidad"] = "Ingresa una cantidad válida mayor o igual a cero."
        }

        val stockMinimo = stockMinimoText.toIntOrNull()
        if (stockMinimo == null || stockMinimo < 0) {
            errores["stockMinimo"] = "Ingresa un nivel de stock mínimo válido (>= 0)."
        }

        if (ubicacion.trim().isEmpty()) {
            errores["ubicacion"] = "La ubicación en almacén es obligatoria."
        }

        return ResultadoValidacion(
            esValido = errores.isEmpty(),
            errores = errores
        )
    }

    fun validarMovimiento(
        cantidadText: String,
        motivo: String,
        usuarioResponsable: String,
        stockDisponible: Int,
        esSalida: Boolean
    ): ResultadoValidacion {
        val errores = mutableMapOf<String, String>()

        val cantidad = cantidadText.toIntOrNull()
        if (cantidad == null || cantidad <= 0) {
            errores["cantidad"] = "Ingresa una cantidad entera mayor a cero."
        } else if (esSalida && cantidad > stockDisponible) {
            errores["cantidad"] = "La cantidad a retirar excede el stock disponible ($stockDisponible)."
        }

        if (motivo.trim().length < 3) {
            errores["motivo"] = "El motivo debe ser explicativo (mínimo 3 caracteres)."
        }

        if (usuarioResponsable.trim().isEmpty()) {
            errores["usuario"] = "El usuario o perfil responsable es obligatorio."
        }

        return ResultadoValidacion(
            esValido = errores.isEmpty(),
            errores = errores
        )
    }

    fun validarLogin(user: String, pass: String): ResultadoValidacion {
        val errores = mutableMapOf<String, String>()

        if (user.trim().isEmpty()) {
            errores["user"] = "Ingresa tu usuario."
        }

        if (pass.trim().isEmpty()) {
            errores["pass"] = "Ingresa tu contraseña."
        }

        return ResultadoValidacion(
            esValido = errores.isEmpty(),
            errores = errores
        )
    }
}
