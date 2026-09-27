package com.example.cappucare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cappucare.ui.screens.PantallaAlertas
import com.example.cappucare.ui.screens.PantallaCatalogo
import com.example.cappucare.ui.screens.PantallaMovimientos

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Catalog.route,
        modifier = modifier
    ) {
        composable(Screen.Catalog.route) {
            PantallaCatalogo(
                onProductClick = { productId ->
                },
                onAddProductClick = {
                }
            )
        }

        composable(Screen.Movements.route) {
            PantallaMovimientos()
        }

        composable(Screen.Alerts.route) {
            PantallaAlertas(
                onProductClick = { productId ->
                }
            )
        }
    }
}
