package com.lksnext.parkingmbakaikoa.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ── Colores primarios ──────────────────────────────────────────────────────
/** Color principal de la app — naranja (desde Figma) */
val PrimaryColor = Color(0xFFFF9800)

/** Variante más clara del primario */
val PrimaryLight = Color(0xFFFFB74D)

/** Variante más oscura del primario */
val PrimaryDark = Color(0xFFF57C00)

// ── Colores secundarios ────────────────────────────────────────────────────
/** Color secundario — gris (desde Figma) */
val SecondaryColor = Color(0xFF37474F)

/** Variante clara del secundario */
val SecondaryLight = Color(0xFF62727B)

/** Variante oscura del secundario */
val SecondaryDark = Color(0xFF102027)

// ── Colores de estado ──────────────────────────────────────────────────────
/** Color para errores */
val ErrorColor = Color(0xFFD32F2F)

/** Color para éxito / positivo */
val SuccessColor = Color(0xFF388E3C)

/** Color para advertencias */
val WarningColor = Color(0xFFFFA726)

/** Color para información */
val InfoColor = Color(0xFF1976D2)

// ── Colores neutros ───────────────────────────────────────────────────────
/** Fondo claro */
val BackgroundLight = Color(0xFAFAFAFA)

/** Fondo oscuro */
val BackgroundDark = Color(0xFF121212)

/** Superficie clara */
val SurfaceLight = Color(0xFFFFFFFF)

/** Superficie oscura */
val SurfaceDark = Color(0xFF1E1E1E)

/** Texto primario */
val TextPrimary = Color(0xFF212121)

/** Texto secundario */
val TextSecondary = Color(0xFF757575)

/** Borde / divisor */
val BorderColor = Color(0xFFE0E0E0)

// ── Esquema de colores para tema claro ─────────────────────────────────────
val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = Color.White,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = PrimaryDark,
    secondary = SecondaryColor,
    onSecondary = Color.White,
    secondaryContainer = SecondaryLight,
    onSecondaryContainer = SecondaryDark,
    tertiary = InfoColor,
    onTertiary = Color.White,
    error = ErrorColor,
    onError = Color.White,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    outlineVariant = BorderColor,
)

