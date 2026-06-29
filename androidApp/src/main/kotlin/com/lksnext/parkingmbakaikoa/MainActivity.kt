package com.lksnext.parkingmbakaikoa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepositoryImpl
import com.lksnext.parkingmbakaikoa.data.repository.BookingRepositoryImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            App(
                authRepository = AuthRepositoryImpl(),
                bookingRepository = BookingRepositoryImpl()
            )
        }
    }
}
