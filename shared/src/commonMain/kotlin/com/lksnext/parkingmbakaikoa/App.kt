package com.lksnext.parkingmbakaikoa

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepository
import com.lksnext.parkingmbakaikoa.ui.auth.AuthScreen
import com.lksnext.parkingmbakaikoa.ui.auth.AuthStateViewModel
import com.lksnext.parkingmbakaikoa.ui.home.HomeScreen
import com.lksnext.parkingmbakaikoa.ui.login.LoginScreen
import com.lksnext.parkingmbakaikoa.ui.login.LoginUiState
import com.lksnext.parkingmbakaikoa.ui.login.LoginViewModel
import com.lksnext.parkingmbakaikoa.ui.register.RegisterScreen
import com.lksnext.parkingmbakaikoa.ui.register.RegisterUiState
import com.lksnext.parkingmbakaikoa.ui.register.RegisterViewModel
import com.lksnext.parkingmbakaikoa.ui.theme.ParkingAppTheme

@Composable
fun App(authRepository: AuthRepository) {
    ParkingAppTheme {
        val authStateViewModel = viewModel { AuthStateViewModel(authRepository) }
        val isLoggedIn by authStateViewModel.isLoggedIn.collectAsState()

        val loginViewModel = viewModel { LoginViewModel(authRepository) }
        val loginUiState by loginViewModel.uiState.collectAsState()

        val registerViewModel = viewModel { RegisterViewModel(authRepository) }
        val registerUiState by registerViewModel.uiState.collectAsState()

        var currentAuthScreen by remember { mutableStateOf(AuthScreen.LOGIN) }

        // Resetear estado de login después de éxito
        if (loginUiState is LoginUiState.Success) {
            loginViewModel.resetState()
        }

        // Resetear estado de registro después de éxito y cambiar a home
        if (registerUiState is RegisterUiState.Success) {
            registerViewModel.resetState()
        }

        when (isLoggedIn) {
            true -> HomeScreen(authStateViewModel = authStateViewModel)
            false -> {
                when (currentAuthScreen) {
                    AuthScreen.LOGIN -> LoginScreen(
                        viewModel = loginViewModel,
                        onCreateAccountClick = {
                            loginViewModel.resetState()
                            currentAuthScreen = AuthScreen.REGISTER
                        }
                    )
                    AuthScreen.REGISTER -> RegisterScreen(
                        viewModel = registerViewModel,
                        onBackToLogin = {
                            registerViewModel.resetState()
                            currentAuthScreen = AuthScreen.LOGIN
                        }
                    )
                }
            }
            null -> {
                // Cargando estado de autenticación, no mostrar nada
            }
        }
    }
}