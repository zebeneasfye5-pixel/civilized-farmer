package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AgroDarkGreenPrimary,
    onPrimary = AgroDarkGreenOnPrimary,
    primaryContainer = AgroDarkGreenContainer,
    onPrimaryContainer = AgroDarkGreenOnContainer,
    secondary = AgroDarkGoldSecondary,
    tertiary = AgroSoilTertiary,
    background = AgroDarkBackground,
    surface = AgroDarkSurface,
    onSurface = AgroDarkOnSurface
)

private val LightColorScheme = lightColorScheme(
    primary = AgroGreenPrimary,
    onPrimary = AgroGreenOnPrimary,
    primaryContainer = AgroGreenContainer,
    onPrimaryContainer = AgroGreenOnContainer,
    secondary = AgroGoldSecondary,
    onSecondary = AgroGoldOnSecondary,
    secondaryContainer = AgroGoldContainer,
    onSecondaryContainer = AgroGoldOnContainer,
    tertiary = AgroSoilTertiary,
    onTertiary = AgroSoilOnTertiary,
    tertiaryContainer = AgroSoilContainer,
    onTertiaryContainer = AgroSoilOnContainer,
    background = AgroBackground,
    onBackground = AgroOnBackground,
    surface = AgroSurface,
    onSurface = AgroOnSurface,
    surfaceVariant = AgroSurfaceVariant,
    onSurfaceVariant = AgroOnSurfaceVariant,
    outline = AgroOutline
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent agricultural branding
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
