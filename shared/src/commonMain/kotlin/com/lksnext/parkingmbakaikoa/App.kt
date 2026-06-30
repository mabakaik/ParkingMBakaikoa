package com.lksnext.parkingmbakaikoa

import androidx.compose.runtime.Composable
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepository
import com.lksnext.parkingmbakaikoa.data.repository.BookingRepository
import com.lksnext.parkingmbakaikoa.data.repository.UserRepository
import com.lksnext.parkingmbakaikoa.data.repository.VehicleRepository
import com.lksnext.parkingmbakaikoa.ui.ParkingApp
import com.lksnext.parkingmbakaikoa.ui.theme.ParkingAppTheme

@Composable
fun App(
    authRepository: AuthRepository,
    bookingRepository: BookingRepository,
    vehicleRepository: VehicleRepository,
    userRepository: UserRepository
) {
    ParkingAppTheme {
        ParkingApp(
            authRepository = authRepository,
            bookingRepository = bookingRepository,
            vehicleRepository = vehicleRepository,
            userRepository = userRepository
        )
    }
}