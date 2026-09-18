package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// SATURN C Luxury Cinematic Palette (Default Obsidian)
val SaturnObsidian = Color(0xFF07080B)
val SaturnDeepSpace = Color(0xFF0A0C12)
val SaturnSurface = Color(0xFF11141D)
val SaturnSurfaceCard = Color(0xFF171B26)
val SaturnSurfaceCardLight = Color(0xFF1F2433)
val SaturnBorder = Color(0xFF262C3D)
val SaturnBorderHighlight = Color(0xFF3B445E)

// Luminous Accents
val SaturnGold = Color(0xFFF5A623)
val SaturnGoldBright = Color(0xFFFFBE42)
val SaturnGoldDark = Color(0xFFD48B12)
val SaturnCyan = Color(0xFF38BDF8)
val SaturnViolet = Color(0xFFA78BFA)
val SaturnRose = Color(0xFFFB7185)
val SaturnEmerald = Color(0xFF34D399)

// High-contrast Neutrals
val SaturnTextPrimary = Color(0xFFF8FAFC)
val SaturnTextSecondary = Color(0xFF94A3B8)
val SaturnTextTertiary = Color(0xFF64748B)

// Glass effect overlays
val GlassBackground = Color(0xCC0A0C12)
val GlassBorder = Color(0x33FFFFFF)
val GlassSurface = Color(0x99171B26)

/**
 * Supported Studio Pastel & Cinematic Themes
 */
enum class SaturnThemePalette(
    val label: String,
    val previewColor: Color,
    val secondaryPreview: Color
) {
    OBSIDIAN_GOLD("Obsidian Gold", Color(0xFFF5A623), Color(0xFF38BDF8)),
    PASTEL_LAVENDER("Pastel Lavender", Color(0xFFC4B5FD), Color(0xFFA78BFA)),
    PASTEL_MINT("Pastel Mint", Color(0xFF6EE7B7), Color(0xFF34D399)),
    PASTEL_ROSE("Pastel Rose", Color(0xFFFDA4AF), Color(0xFFFB7185)),
    PASTEL_PEACH("Pastel Peach", Color(0xFFFDBA74), Color(0xFFFB923C)),
    PASTEL_SKY("Pastel Sky", Color(0xFF7DD3FC), Color(0xFF38BDF8))
}

/**
 * Dynamic Design Tokens for Theme & Light/Dark Modes
 */
data class SaturnColors(
    val background: Color,
    val surface: Color,
    val surfaceCard: Color,
    val surfaceCardLight: Color,
    val border: Color,
    val borderHighlight: Color,
    val primaryAccent: Color,
    val secondaryAccent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val isDark: Boolean
)

val LocalSaturnColors = staticCompositionLocalOf {
    getSaturnColors(SaturnThemePalette.OBSIDIAN_GOLD, true)
}

val LocalCompactMode = staticCompositionLocalOf { false }

fun getSaturnColors(palette: SaturnThemePalette, isDark: Boolean): SaturnColors {
    return when (palette) {
        SaturnThemePalette.OBSIDIAN_GOLD -> if (isDark) {
            SaturnColors(
                background = Color(0xFF07080B),
                surface = Color(0xFF11141D),
                surfaceCard = Color(0xFF171B26),
                surfaceCardLight = Color(0xFF1F2433),
                border = Color(0xFF262C3D),
                borderHighlight = Color(0xFF3B445E),
                primaryAccent = Color(0xFFF5A623),
                secondaryAccent = Color(0xFF38BDF8),
                textPrimary = Color(0xFFF8FAFC),
                textSecondary = Color(0xFF94A3B8),
                textTertiary = Color(0xFF64748B),
                isDark = true
            )
        } else {
            SaturnColors(
                background = Color(0xFFFAF8F5),
                surface = Color(0xFFF3ECE1),
                surfaceCard = Color(0xFFFFFFFF),
                surfaceCardLight = Color(0xFFF8F4EE),
                border = Color(0xFFE2D7C5),
                borderHighlight = Color(0xFFF5A623),
                primaryAccent = Color(0xFFD97706),
                secondaryAccent = Color(0xFF0284C7),
                textPrimary = Color(0xFF1C1917),
                textSecondary = Color(0xFF78716C),
                textTertiary = Color(0xFFA8A29E),
                isDark = false
            )
        }

        SaturnThemePalette.PASTEL_LAVENDER -> if (isDark) {
            SaturnColors(
                background = Color(0xFF0E0C17),
                surface = Color(0xFF161324),
                surfaceCard = Color(0xFF1F1B33),
                surfaceCardLight = Color(0xFF2C2646),
                border = Color(0xFF382F57),
                borderHighlight = Color(0xFF564887),
                primaryAccent = Color(0xFFC4B5FD),
                secondaryAccent = Color(0xFFA78BFA),
                textPrimary = Color(0xFFF5F3FF),
                textSecondary = Color(0xFFB8B0D6),
                textTertiary = Color(0xFF867BA9),
                isDark = true
            )
        } else {
            SaturnColors(
                background = Color(0xFFFAF7FF),
                surface = Color(0xFFF1EBFF),
                surfaceCard = Color(0xFFFFFFFF),
                surfaceCardLight = Color(0xFFF5F0FF),
                border = Color(0xFFE0D4FC),
                borderHighlight = Color(0xFFC4B5FD),
                primaryAccent = Color(0xFF8B5CF6),
                secondaryAccent = Color(0xFF7C3AED),
                textPrimary = Color(0xFF1E143B),
                textSecondary = Color(0xFF6D638F),
                textTertiary = Color(0xFF9D95BC),
                isDark = false
            )
        }

        SaturnThemePalette.PASTEL_MINT -> if (isDark) {
            SaturnColors(
                background = Color(0xFF091410),
                surface = Color(0xFF0F1E19),
                surfaceCard = Color(0xFF162B24),
                surfaceCardLight = Color(0xFF1E3A31),
                border = Color(0xFF274A3E),
                borderHighlight = Color(0xFF386B5A),
                primaryAccent = Color(0xFF6EE7B7),
                secondaryAccent = Color(0xFF34D399),
                textPrimary = Color(0xFFECFDF5),
                textSecondary = Color(0xFFA3C7B7),
                textTertiary = Color(0xFF729A88),
                isDark = true
            )
        } else {
            SaturnColors(
                background = Color(0xFFF2FBF6),
                surface = Color(0xFFE2F7ED),
                surfaceCard = Color(0xFFFFFFFF),
                surfaceCardLight = Color(0xFFEEFBF4),
                border = Color(0xFFC6EED9),
                borderHighlight = Color(0xFF6EE7B7),
                primaryAccent = Color(0xFF10B981),
                secondaryAccent = Color(0xFF059669),
                textPrimary = Color(0xFF0F2C20),
                textSecondary = Color(0xFF527D6C),
                textTertiary = Color(0xFF84A89A),
                isDark = false
            )
        }

        SaturnThemePalette.PASTEL_ROSE -> if (isDark) {
            SaturnColors(
                background = Color(0xFF160B0F),
                surface = Color(0xFF231218),
                surfaceCard = Color(0xFF311922),
                surfaceCardLight = Color(0xFF42222E),
                border = Color(0xFF542B3A),
                borderHighlight = Color(0xFF783E53),
                primaryAccent = Color(0xFFFDA4AF),
                secondaryAccent = Color(0xFFFB7185),
                textPrimary = Color(0xFFFFF1F2),
                textSecondary = Color(0xFFD4A8B2),
                textTertiary = Color(0xFFA87A86),
                isDark = true
            )
        } else {
            SaturnColors(
                background = Color(0xFFFFF5F6),
                surface = Color(0xFFFFE6E9),
                surfaceCard = Color(0xFFFFFFFF),
                surfaceCardLight = Color(0xFFFFF0F2),
                border = Color(0xFFFDCFD6),
                borderHighlight = Color(0xFFFDA4AF),
                primaryAccent = Color(0xFFF43F5E),
                secondaryAccent = Color(0xFFE11D48),
                textPrimary = Color(0xFF380E1A),
                textSecondary = Color(0xFF8C5B67),
                textTertiary = Color(0xFFB58490),
                isDark = false
            )
        }

        SaturnThemePalette.PASTEL_PEACH -> if (isDark) {
            SaturnColors(
                background = Color(0xFF160F09),
                surface = Color(0xFF23190F),
                surfaceCard = Color(0xFF312316),
                surfaceCardLight = Color(0xFF43301E),
                border = Color(0xFF573E28),
                borderHighlight = Color(0xFF7A5839),
                primaryAccent = Color(0xFFFDBA74),
                secondaryAccent = Color(0xFFFB923C),
                textPrimary = Color(0xFFFFF7ED),
                textSecondary = Color(0xFFCFAF97),
                textTertiary = Color(0xFFA3826A),
                isDark = true
            )
        } else {
            SaturnColors(
                background = Color(0xFFFFF8F1),
                surface = Color(0xFFFFEEDD),
                surfaceCard = Color(0xFFFFFFFF),
                surfaceCardLight = Color(0xFFFFF3E8),
                border = Color(0xFFFED7AA),
                borderHighlight = Color(0xFFFDBA74),
                primaryAccent = Color(0xFFF97316),
                secondaryAccent = Color(0xFFEA580C),
                textPrimary = Color(0xFF381B07),
                textSecondary = Color(0xFF8A6242),
                textTertiary = Color(0xFFB38D6F),
                isDark = false
            )
        }

        SaturnThemePalette.PASTEL_SKY -> if (isDark) {
            SaturnColors(
                background = Color(0xFF091118),
                surface = Color(0xFF0E1A26),
                surfaceCard = Color(0xFF152636),
                surfaceCardLight = Color(0xFF1E344A),
                border = Color(0xFF27435F),
                borderHighlight = Color(0xFF375E85),
                primaryAccent = Color(0xFF7DD3FC),
                secondaryAccent = Color(0xFF38BDF8),
                textPrimary = Color(0xFFF0F9FF),
                textSecondary = Color(0xFFA0BFD6),
                textTertiary = Color(0xFF6F92AC),
                isDark = true
            )
        } else {
            SaturnColors(
                background = Color(0xFFF2FAFF),
                surface = Color(0xFFE1F3FE),
                surfaceCard = Color(0xFFFFFFFF),
                surfaceCardLight = Color(0xFFECF7FE),
                border = Color(0xFFBEE4FC),
                borderHighlight = Color(0xFF7DD3FC),
                primaryAccent = Color(0xFF0284C7),
                secondaryAccent = Color(0xFF0369A1),
                textPrimary = Color(0xFF08273D),
                textSecondary = Color(0xFF486E8C),
                textTertiary = Color(0xFF7D9DB8),
                isDark = false
            )
        }
    }
}

