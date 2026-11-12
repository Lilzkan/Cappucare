package com.example.cappucare.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.cappucare.data.model.RolUsuario
import com.example.cappucare.data.model.Usuario

class SesionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("cappucare_session_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USERNAME = "key_username"
        private const val KEY_NOMBRE = "key_nombre"
        private const val KEY_ROL = "key_rol"
        private const val KEY_TOKEN = "key_token"

        // Usuarios Semilla para Fallback Offline
        val USUARIOS_SEMILLA = listOf(
            Triple("admin", "admin123", Usuario("admin", "Administrador Principal", RolUsuario.ADMIN, "token_admin_mock")),
            Triple("trabajador1", "work123", Usuario("trabajador1", "Juan Pérez (Bodega)", RolUsuario.TRABAJADOR, "token_work1_mock")),
            Triple("trabajador2", "work123", Usuario("trabajador2", "Paz Morales (Enfermería)", RolUsuario.TRABAJADOR, "token_work2_mock"))
        )
    }

    fun guardarSesion(usuario: Usuario) {
        prefs.edit()
            .putString(KEY_USERNAME, usuario.username)
            .putString(KEY_NOMBRE, usuario.nombreCompleto)
            .putString(KEY_ROL, usuario.rol.name)
            .putString(KEY_TOKEN, usuario.token)
            .apply()
    }

    fun obtenerUsuarioActual(): Usuario? {
        val username = prefs.getString(KEY_USERNAME, null) ?: return null
        val nombre = prefs.getString(KEY_NOMBRE, "Usuario Cappucare") ?: "Usuario Cappucare"
        val rolStr = prefs.getString(KEY_ROL, RolUsuario.TRABAJADOR.name) ?: RolUsuario.TRABAJADOR.name
        val token = prefs.getString(KEY_TOKEN, null)

        val rol = try {
            RolUsuario.valueOf(rolStr)
        } catch (_: Exception) {
            RolUsuario.TRABAJADOR
        }

        return Usuario(
            username = username,
            nombreCompleto = nombre,
            rol = rol,
            token = token
        )
    }

    fun obtenerTokenJwt(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun cerrarSesion() {
        prefs.edit().clear().apply()
    }

    fun validarLoginOffline(user: String, pass: String): Usuario? {
        val encontrado = USUARIOS_SEMILLA.find {
            it.first.equals(user.trim(), ignoreCase = true) && it.second == pass.trim()
        }
        return encontrado?.third
    }
}
