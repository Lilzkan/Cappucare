package com.example.cappucare.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = TealOnPrimary,
    primaryContainer = TealContainer,
    onPrimaryContainer = TealOnContainer,
    secondary = BlueSecondary,
    onSecondary = BlueOnSecondary,
    secondaryContainer = BlueSecondaryContainer,
    onSecondaryContainer = BlueOnSecondaryContainer,
    error = RedStockError,
    errorContainer = RedErrorContainer,
    onErrorContainer = RedOnErrorContainer,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight
)

private val DarkColorScheme = darkColorScheme(
    primary = TealPrimaryDark,
    primaryContainer = TealContainerDark,
    error = RedStockError,
    errorContainer = RedErrorContainer
)

@Composable
fun CappucareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}