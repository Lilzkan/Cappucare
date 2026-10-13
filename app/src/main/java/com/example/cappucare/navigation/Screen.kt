package com.example.cappucare.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector? = null
) {
    object Catalogo : Screen("catalog", "Catálogo", Icons.Filled.Inventory)
    object Historial : Screen("movements", "Historial", Icons.Filled.History)
    object Alertas : Screen("alerts", "Stock Bajo", Icons.Filled.Warning)

    object DetalleProducto : Screen("product_detail/{productId}", "Detalle") {
        fun crearRuta(productId: String) = "product_detail/$productId"
    }
    object AgregarProducto : Screen("add_product", "Nuevo Producto")
    object EditarProducto : Screen("edit_product/{productId}", "Editar Producto") {
        fun crearRuta(productId: String) = "edit_product/$productId"
    }
    object RegistrarMovimiento : Screen("register_movement/{productId}?tipo={tipo}", "Registrar Movimiento") {
        fun crearRuta(productId: String, tipo: String = "") = "register_movement/$productId?tipo=$tipo"
    }
}

val pantallasNavegacionInferior = listOf(
    Screen.Catalogo,
    Screen.Historial,
    Screen.Alertas
)