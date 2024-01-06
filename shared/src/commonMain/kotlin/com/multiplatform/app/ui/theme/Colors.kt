package com.multiplatform.app.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color



// color that are same in Light and Dark

val prepaidIndigo = Color(0xFF21DED7)
val prepaidIndigoDark = Color(0xFF0F7572)
val prepaidYellow = Color(0xFFFFF264)
val prepaidGreen = Color(0xFF00B379)
val prepaidErrorColor = Color(0xFFBC1430)


val PrepaidDarkColors = darkColorScheme(
    primary = darkPrimary,
    secondary = darkSecondary,
    tertiary = darkTertiary,
    onPrimary = darkOnPrimary,
    primaryContainer = darkPrimaryContainer,
    onPrimaryContainer = darkOnPrimaryContainer,
    onSecondary = darkOnSecondary,
    secondaryContainer = darkSecondaryContainer,
    onSecondaryContainer = darkOnSecondaryContainer,
    onTertiary = darkOnTertiary,
    onTertiaryContainer = darkOnTertiaryContainer,
    tertiaryContainer = darkTertiaryContainer,
    //background = darkBackground,
    background = transparent,
    onBackground = darkOnBackground,
    surface = darkSurface,
    onSurface = darkOnSurface,
    surfaceVariant = darkSurfaceVariant,
    onSurfaceVariant = darkOnSurfaceVariant,
    error = darkError,
    onError = darkOnError,
    errorContainer = darkErrorContainer,
    onErrorContainer = darkOnErrorContainer,
    outline = darkOutline,
)

val PrepaidLightColors = darkColorScheme(
    primary = lightPrimary,
    secondary = lightSecondary,
    tertiary = lightTertiary,
    onPrimary = lightOnPrimary,
    primaryContainer = lightPrimaryContainer,
    onPrimaryContainer = lightOnPrimaryContainer,
    onSecondary = lightOnSecondary,
    secondaryContainer = lightSecondaryContainer,
    onSecondaryContainer = lightOnSecondaryContainer,
    onTertiary = lightOnTertiary,
    onTertiaryContainer = lightOnTertiaryContainer,
    tertiaryContainer = lightTertiaryContainer,
    //background = lightBackground,
    background = transparent,
    onBackground = lightOnBackground,
    surface = lightSurface,
    onSurface = lightOnSurface,
    surfaceVariant = lightSurfaceVariant,
    onSurfaceVariant = lightOnSurfaceVariant,
    error = lightError,
    onError = lightOnError,
    errorContainer = lightErrorContainer,
    onErrorContainer = lightOnErrorContainer,
    outline = lightOutline,
)