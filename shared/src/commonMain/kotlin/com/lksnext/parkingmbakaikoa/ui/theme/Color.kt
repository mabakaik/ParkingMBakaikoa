package com.lksnext.parkingmbakaikoa.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ── Colores primarios ──────────────────────────────────────────────────────
val PrimaryColor = Color(0xFFFF9800)
val PrimaryLight = Color(0xFFFFB74D)
val PrimaryDark = Color(0xFFF57C00)
val primaryBackground = Color(0xFFFFE0B2)

// ── Colores secundarios ────────────────────────────────────────────────────
val SecondaryColor = Color(0xFF37474F)
val SecondaryLight = Color(0xFF62727B)
val SecondaryDark = Color(0xFF102027)

// ── Colores de estado ──────────────────────────────────────────────────────
val ErrorColor = Color(0xFFD32F2F)

val SuccessColor = Color(0xFF388E3C)

val WarningColor = Color(0xFFFFA726)
val InfoColor = Color(0xFF1976D2)

// ── Colores de textos ───────────────────────────────────────────────────────
val subtitleGreyMedium = Color(0xFF757575)

// ── Colores neutros ───────────────────────────────────────────────────────
val BackgroundLight = Color(0xFAFAFAFA)

val BackgroundDark = Color(0xFF121212)

val SurfaceLight = Color(0xFFFFFFFF)

val SurfaceDark = Color(0xFF1E1E1E)

val TextPrimary = Color(0xFF212121)

val TextSecondary = Color(0xFF757575)

val BorderColor = Color(0xFFE0E0E0)

// ── Colores de estado de Reservas (Bookings) ───────────────────────────────
// Confirmada
val BookingConfirmedBackground = Color(0xFFC8E6C9)
val BookingConfirmedText = Color(0xFF2E7D32)

// En Curso
val BookingInProgressBackground = Color(0xFFFFE0B2)
val BookingInProgressText = Color(0xFFE65100)

// Cancelada
val BookingCancelledBackground = Color(0xFFFFCDD2)
val BookingCancelledText = Color(0xFFC62828)

// Terminada
val BookingCompletedBackground = Color(0xFFE0E0E0)
val BookingCompletedText = Color(0xFF424242)

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

