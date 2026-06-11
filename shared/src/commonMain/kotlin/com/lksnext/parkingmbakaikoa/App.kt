package com.lksnext.parkingmbakaikoa

import androidx.compose.runtime.Composable
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepository
import com.lksnext.parkingmbakaikoa.ui.ParkingApp
import com.lksnext.parkingmbakaikoa.ui.theme.ParkingAppTheme

@Composable
fun App(authRepository: AuthRepository) {
    ParkingAppTheme {
        ParkingApp(authRepository)
    }
}