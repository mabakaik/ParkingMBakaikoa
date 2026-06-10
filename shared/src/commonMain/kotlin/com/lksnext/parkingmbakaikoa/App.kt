package com.lksnext.parkingmbakaikoa

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepository
import com.lksnext.parkingmbakaikoa.ui.home.HomeScreen
import com.lksnext.parkingmbakaikoa.ui.login.LoginScreen
import com.lksnext.parkingmbakaikoa.ui.login.LoginUiState
import com.lksnext.parkingmbakaikoa.ui.login.LoginViewModel
import com.lksnext.parkingmbakaikoa.ui.navigation.NavigationRoute
import com.lksnext.parkingmbakaikoa.ui.theme.ParkingAppTheme

@Composable
fun App(authRepository: AuthRepository) {
    ParkingAppTheme {
        val currentRoute = remember { mutableStateOf<NavigationRoute>(NavigationRoute.Login) }
        val loginViewModel = viewModel { LoginViewModel(authRepository) }
        val loginUiState by loginViewModel.uiState.collectAsState()

        if (loginUiState is LoginUiState.Success) {
            currentRoute.value = NavigationRoute.Home
        }

        when (currentRoute.value) {
            NavigationRoute.Login -> LoginScreen(viewModel = loginViewModel)
            NavigationRoute.Home -> HomeScreen()
            else -> LoginScreen(viewModel = loginViewModel)
        }
    }
}