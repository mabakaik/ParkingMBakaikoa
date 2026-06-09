package com.lksnext.parkingmbakaikoa

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepository
import com.lksnext.parkingmbakaikoa.ui.login.LoginScreen
import com.lksnext.parkingmbakaikoa.ui.login.LoginViewModel
import com.lksnext.parkingmbakaikoa.ui.theme.ParkingAppTheme

@Composable
fun App(authRepository: AuthRepository) {
    ParkingAppTheme {
        val loginViewModel = viewModel { LoginViewModel(authRepository) }
        LoginScreen(viewModel = loginViewModel)
    }
}