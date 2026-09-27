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
    object Catalog : Screen("catalog", "Catálogo", Icons.Filled.Inventory)
    object Movements : Screen("movements", "Historial", Icons.Filled.History)
    object Alerts : Screen("alerts", "Stock Bajo", Icons.Filled.Warning)

    object ProductDetail : Screen("product_detail/{productId}", "Detalle") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    object AddProduct : Screen("add_product", "Nuevo Producto")
    object RegisterMovement : Screen("register_movement/{productId}", "Registrar Movimiento") {
        fun createRoute(productId: String) = "register_movement/$productId"
    }
}

val bottomNavScreens = listOf(
    Screen.Catalog,
    Screen.Movements,
    Screen.Alerts
)
