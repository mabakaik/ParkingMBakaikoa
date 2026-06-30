package com.lksnext.parkingmbakaikoa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepositoryImpl
import com.lksnext.parkingmbakaikoa.data.repository.BookingRepositoryImpl
import com.lksnext.parkingmbakaikoa.data.repository.UserRepositoryImpl
import com.lksnext.parkingmbakaikoa.data.repository.VehicleRepositoryImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Instalar el splash screen nativo de Android (debe ser lo primero)
        installSplashScreen()

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            App(
                authRepository = AuthRepositoryImpl(),
                bookingRepository = BookingRepositoryImpl(),
                vehicleRepository = VehicleRepositoryImpl(),
                userRepository = UserRepositoryImpl()
            )
        }
    }
}
