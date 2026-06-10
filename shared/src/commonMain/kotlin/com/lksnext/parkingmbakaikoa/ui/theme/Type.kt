package com.lksnext.parkingmbakaikoa.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import org.jetbrains.compose.resources.Font

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import parkingmbakaikoa.shared.generated.resources.Inter_18pt_Bold
import parkingmbakaikoa.shared.generated.resources.Inter_18pt_Regular
import parkingmbakaikoa.shared.generated.resources.Inter_18pt_SemiBold
import parkingmbakaikoa.shared.generated.resources.Res

@Composable
fun interFontFamily() = FontFamily(
        Font(Res.font.Inter_18pt_Regular, FontWeight.Normal),
        Font(Res.font.Inter_18pt_SemiBold, FontWeight.SemiBold),
        Font(Res.font.Inter_18pt_Bold, FontWeight.Bold),
    )

@Composable
fun parkingAppTypography() = Typography(
    // Títulos grandes (para encabezados principales)
    headlineLarge = TextStyle(
        fontFamily = interFontFamily(),
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    // Títulos medianos
    headlineMedium = TextStyle(
        fontFamily = interFontFamily(),
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
    ),
    // Títulos pequeños
    headlineSmall = TextStyle(
        fontFamily = interFontFamily(),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    // Título de sección
    titleLarge = TextStyle(
        fontFamily = interFontFamily(),
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    // Subtítulo mediano
    titleMedium = TextStyle(
        fontFamily = interFontFamily(),
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    // Subtítulo pequeño
    titleSmall = TextStyle(
        fontFamily = interFontFamily(),
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    // Cuerpo grande
    bodyLarge = TextStyle(
        fontFamily = interFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    // Cuerpo mediano
    bodyMedium = TextStyle(
        fontFamily = interFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    // Cuerpo pequeño
    bodySmall = TextStyle(
        fontFamily = interFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    // Etiqueta grande (para botones, tags)
    labelLarge = TextStyle(
        fontFamily = interFontFamily(),
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    // Etiqueta mediana
    labelMedium = TextStyle(
        fontFamily = interFontFamily(),
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    // Etiqueta pequeña
    labelSmall = TextStyle(
        fontFamily = interFontFamily(),
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
    ),
)
