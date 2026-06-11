package com.lksnext.parkingmbakaikoa.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ── Radio de esquinas personalizadas ───────────────────────────────────────
/** Esquinas muy pequeñas (2dp) — para elementos mínimos */
val ExtraSmallRadius = 2.dp

/** Esquinas pequeñas (4dp) — para inputs, chips */
val SmallRadius = 4.dp

/** Esquinas medianas (8dp) — para botones, cards pequeños */
val MediumRadius = 8.dp

/** Esquinas grandes (12dp) — para cards medianas, dialogs */
val LargeRadius = 12.dp

/** Esquinas extra grandes (16dp) — para componentes principales */
val ExtraLargeRadius = 16.dp

/** Esquinas completamente redondeadas (50%) — para botones FAB */
val FullyRoundedRadius = 50.dp

// ── Esquema de formas para Material 3 ──────────────────────────────────────
val ParkingAppShapes = Shapes(
    extraSmall = RoundedCornerShape(ExtraSmallRadius),
    small = RoundedCornerShape(SmallRadius),
    medium = RoundedCornerShape(MediumRadius),
    large = RoundedCornerShape(LargeRadius),
    extraLarge = RoundedCornerShape(ExtraLargeRadius),
)

// ── Formas específicas para componentes ────────────────────────────────────
/** Forma para inputs y campos de texto */
val InputShape = RoundedCornerShape(MediumRadius)

/** Forma para botones principales */
val ButtonShape = RoundedCornerShape(MediumRadius)

/** Forma para cards */
val CardShape = RoundedCornerShape(LargeRadius)

/** Forma para dialogs modales */
val DialogShape = RoundedCornerShape(ExtraLargeRadius)

/** FAB (Floating Action Button) */
val FabShape = RoundedCornerShape(FullyRoundedRadius)

