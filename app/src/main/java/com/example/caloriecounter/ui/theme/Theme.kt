package com.example.caloriecounter.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = GreenSecondary,
    secondary = OrangeAccent,
    surface = SurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = OrangeAccent,
    surface = SurfaceLight
)

@Composable
fun CalorieCounterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
