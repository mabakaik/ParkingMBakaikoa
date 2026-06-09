package com.lksnext.parkingmbakaikoa.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

// ── Colores primarios ──────────────────────────────────────────────────────
/** Color principal de la app — azul moderno */
val PrimaryColor = Color(0xFF0066CC)

/** Variante más clara del primario */
val PrimaryLight = Color(0xFF4D99E6)

/** Variante más oscura del primario */
val PrimaryDark = Color(0xFF004A99)

// ── Colores secundarios ────────────────────────────────────────────────────
/** Color secundario — naranja (para acciones complementarias) */
val SecondaryColor = Color(0xFFFF9800)

/** Variante clara del secundario */
val SecondaryLight = Color(0xFFFFB84D)

/** Variante oscura del secundario */
val SecondaryDark = Color(0xFFCC7A00)

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
val BackgroundLight = Color(0xFFFAFAFA)

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

// ── Esquema de colores para tema oscuro ────────────────────────────────────
val DarkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    onPrimary = PrimaryDark,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = PrimaryLight,
    secondary = SecondaryLight,
    onSecondary = SecondaryDark,
    secondaryContainer = SecondaryDark,
    onSecondaryContainer = SecondaryLight,
    tertiary = Color(0xFF64B5F6),
    onTertiary = Color.Black,
    error = Color(0xFFFF6B6B),
    onError = Color.Black,
    background = BackgroundDark,
    onBackground = Color.White,
    surface = SurfaceDark,
    onSurface = Color.White,
    outlineVariant = Color(0xFF424242),
)

