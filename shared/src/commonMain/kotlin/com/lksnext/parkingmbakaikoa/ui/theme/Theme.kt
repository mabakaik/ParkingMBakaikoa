package com.lksnext.parkingmbakaikoa.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * Tema principal de la aplicación Parking App.
 *
 * Incluye:
 * - Colores personalizados (tema claro)
 * - Formas (radio de esquinas)
 * - Tipografía coherente en todo el app
 *
 * @param content El contenido que recibirá el tema aplicado.
 */
@Composable
fun ParkingAppTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        shapes = ParkingAppShapes,
        typography = ParkingAppTypography,
        content = content,
    )
}


