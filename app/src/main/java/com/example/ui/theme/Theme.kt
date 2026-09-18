package com.example.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

val SaturnShapes = Shapes(
    extraSmall = CircleShape,
    small = CircleShape,
    medium = RoundedCornerShape(28.dp),
    large = RoundedCornerShape(32.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

object SaturnTheme {
    val colors: SaturnColors
        @Composable
        get() = LocalSaturnColors.current

    val isCompact: Boolean
        @Composable
        get() = LocalCompactMode.current
}

@Composable
fun SaturnTheme(
    palette: SaturnThemePalette = SaturnThemePalette.OBSIDIAN_GOLD,
    darkTheme: Boolean = true,
    isCompact: Boolean = false,
    content: @Composable () -> Unit
) {
    val saturnColors = getSaturnColors(palette, darkTheme)

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = saturnColors.primaryAccent,
            onPrimary = saturnColors.background,
            primaryContainer = saturnColors.surfaceCardLight,
            onPrimaryContainer = saturnColors.primaryAccent,
            secondary = saturnColors.secondaryAccent,
            onSecondary = saturnColors.background,
            secondaryContainer = saturnColors.surfaceCard,
            onSecondaryContainer = saturnColors.secondaryAccent,
            tertiary = saturnColors.primaryAccent,
            onTertiary = saturnColors.background,
            background = saturnColors.background,
            onBackground = saturnColors.textPrimary,
            surface = saturnColors.surface,
            onSurface = saturnColors.textPrimary,
            surfaceVariant = saturnColors.surfaceCard,
            onSurfaceVariant = saturnColors.textSecondary,
            outline = saturnColors.border,
            outlineVariant = saturnColors.borderHighlight
        )
    } else {
        lightColorScheme(
            primary = saturnColors.primaryAccent,
            onPrimary = androidx.compose.ui.graphics.Color.White,
            primaryContainer = saturnColors.surfaceCardLight,
            onPrimaryContainer = saturnColors.primaryAccent,
            secondary = saturnColors.secondaryAccent,
            onSecondary = androidx.compose.ui.graphics.Color.White,
            secondaryContainer = saturnColors.surface,
            onSecondaryContainer = saturnColors.secondaryAccent,
            tertiary = saturnColors.primaryAccent,
            onTertiary = androidx.compose.ui.graphics.Color.White,
            background = saturnColors.background,
            onBackground = saturnColors.textPrimary,
            surface = saturnColors.surface,
            onSurface = saturnColors.textPrimary,
            surfaceVariant = saturnColors.surfaceCard,
            onSurfaceVariant = saturnColors.textSecondary,
            outline = saturnColors.border,
            outlineVariant = saturnColors.borderHighlight
        )
    }

    CompositionLocalProvider(
        LocalSaturnColors provides saturnColors,
        LocalCompactMode provides isCompact
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = SaturnShapes,
            content = content
        )
    }
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    SaturnTheme(darkTheme = darkTheme, content = content)
}

