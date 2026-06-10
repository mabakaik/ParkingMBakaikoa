package com.lksnext.parkingmbakaikoa.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ParkingAppTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        shapes = ParkingAppShapes,
        typography = parkingAppTypography(),
        content = content,
    )
}


