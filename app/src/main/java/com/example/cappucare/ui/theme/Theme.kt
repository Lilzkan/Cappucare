package com.example.cappucare.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = ForestPrimary,
    onPrimary = ForestOnPrimary,
    primaryContainer = ForestPrimaryContainer,
    onPrimaryContainer = ForestOnPrimaryContainer,
    secondary = SageSecondary,
    onSecondary = SageOnSecondary,
    secondaryContainer = ForestPrimaryContainer,
    onSecondaryContainer = ForestOnPrimaryContainer,
    error = AlertTerracotta,
    errorContainer = AlertTerracottaContainer,
    onErrorContainer = OnAlertTerracottaContainer,
    background = NaturalBackground,
    onBackground = TextDark,
    surface = NaturalSurface,
    onSurface = TextDark,
    surfaceVariant = NaturalSurfaceVariant,
    onSurfaceVariant = TextMedium,
    outline = NaturalOutline,
    outlineVariant = NaturalOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = ForestPrimaryContainer,
    primaryContainer = ForestPrimary,
    error = AlertTerracotta,
    errorContainer = AlertTerracottaContainer,
    background = TextDark,
    onBackground = NaturalBackground,
    surface = TextDark,
    onSurface = NaturalSurface
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