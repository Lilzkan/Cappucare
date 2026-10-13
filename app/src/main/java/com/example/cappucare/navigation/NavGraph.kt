package com.example.cappucare.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cappucare.ui.screens.*
import com.example.cappucare.ui.viewmodel.CappucareViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: CappucareViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Catalogo.route,
        modifier = modifier,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        composable(Screen.Catalogo.route) {
            PantallaCatalogo(
                viewModel = viewModel,
                onProductClick = { productId ->
                    navController.navigate(Screen.DetalleProducto.crearRuta(productId))
                },
                onAddProductClick = {
                    navController.navigate(Screen.AgregarProducto.route)
                }
            )
        }

        composable(Screen.Historial.route) {
            PantallaMovimientos(viewModel = viewModel)
        }

        composable(Screen.Alertas.route) {
            PantallaAlertas(
                viewModel = viewModel,
                onProductClick = { productId ->
                    navController.navigate(Screen.DetalleProducto.crearRuta(productId))
                }
            )
        }

        composable(
            route = Screen.DetalleProducto.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType }),
            enterTransition = {
                fadeIn(animationSpec = tween(300)) +
                        scaleIn(initialScale = 0.88f, animationSpec = tween(300)) +
                        slideInVertically(initialOffsetY = { it / 8 }, animationSpec = tween(300))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(250)) +
                        scaleOut(targetScale = 0.88f, animationSpec = tween(250))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(250)) +
                        scaleIn(initialScale = 0.88f, animationSpec = tween(250))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(300)) +
                        scaleOut(targetScale = 0.88f, animationSpec = tween(300)) +
                        slideOutVertically(targetOffsetY = { it / 8 }, animationSpec = tween(300))
            }
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            PantallaDetalleProducto(
                productId = productId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate(Screen.EditarProducto.crearRuta(id))
                },
                onRegisterMovementClick = { id, tipo ->
                    navController.navigate(Screen.RegistrarMovimiento.crearRuta(id, tipo))
                }
            )
        }

        composable(
            route = Screen.AgregarProducto.route,
            enterTransition = {
                fadeIn(animationSpec = tween(300)) +
                        scaleIn(initialScale = 0.9f, animationSpec = tween(300)) +
                        slideInVertically(initialOffsetY = { it / 6 }, animationSpec = tween(300))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(250)) +
                        slideOutVertically(targetOffsetY = { it / 6 }, animationSpec = tween(250))
            }
        ) {
            PantallaFormularioProducto(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditarProducto.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType }),
            enterTransition = {
                fadeIn(animationSpec = tween(300)) +
                        scaleIn(initialScale = 0.9f, animationSpec = tween(300))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(250)) +
                        scaleOut(targetScale = 0.9f, animationSpec = tween(250))
            }
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            PantallaFormularioProducto(
                productIdToEdit = productId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.RegistrarMovimiento.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType },
                navArgument("tipo") { type = NavType.StringType; defaultValue = "" }
            ),
            enterTransition = {
                fadeIn(animationSpec = tween(300)) +
                        scaleIn(initialScale = 0.9f, animationSpec = tween(300)) +
                        slideInVertically(initialOffsetY = { it / 6 }, animationSpec = tween(300))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(250)) +
                        slideOutVertically(targetOffsetY = { it / 6 }, animationSpec = tween(250))
            }
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val tipo = backStackEntry.arguments?.getString("tipo")
            PantallaRegistrarMovimiento(
                productId = productId,
                initialTipoStr = tipo,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }
    }
}