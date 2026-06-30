package com.lksnext.parkingmbakaikoa.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepository
import com.lksnext.parkingmbakaikoa.data.repository.BookingRepository
import com.lksnext.parkingmbakaikoa.data.repository.UserRepository
import com.lksnext.parkingmbakaikoa.data.repository.VehicleRepository
import com.lksnext.parkingmbakaikoa.ui.auth.AuthStateViewModel
import com.lksnext.parkingmbakaikoa.ui.home.HomeScreen
import com.lksnext.parkingmbakaikoa.ui.login.LoginScreen
import com.lksnext.parkingmbakaikoa.ui.navigation.Routes
import com.lksnext.parkingmbakaikoa.ui.register.RegisterScreen
import com.lksnext.parkingmbakaikoa.ui.resetPassword.ResetPasswordScreen
import com.lksnext.parkingmbakaikoa.ui.theme.ParkingAppTheme

@Composable
fun ParkingApp(
    authRepository: AuthRepository,
    bookingRepository: BookingRepository,
    vehicleRepository: VehicleRepository,
    userRepository: UserRepository
){
    val navController: NavHostController = rememberNavController()
    val authStateViewModel = viewModel { AuthStateViewModel(authRepository) }
    val isLoggedIn by authStateViewModel.isLoggedIn.collectAsState()

    ParkingAppTheme {
        val startDestination = if (isLoggedIn == true) Routes.Home.name else Routes.Login.name

        var isFirstComposition by remember { mutableStateOf(true) }

        LaunchedEffect(isLoggedIn) {
            if (isFirstComposition) {
                isFirstComposition = false
            } else {
                if (isLoggedIn == true) {
                    // Usuario autenticado: navegar a Home
                    navController.navigate(Routes.Home.name) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                } else {
                    // Usuario no autenticado: navegar a Login
                    navController.navigate(Routes.Login.name) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }

        NavHost(
            navController = navController,
            startDestination = startDestination,
        ){
            composable(route = Routes.Login.name){
                LoginScreen(
                    navController = navController,
                    authRepository = authRepository
                )
            }
            composable(route = Routes.Register.name){
                RegisterScreen(
                    navController = navController,
                    authRepository = authRepository
                )
            }
            composable(route = Routes.ResetPassword.name){
                ResetPasswordScreen(
                    navController = navController,
                    authRepository = authRepository
                )
            }
            composable(route = Routes.Home.name){
                HomeScreen(
                    authRepository = authRepository,
                    bookingRepository = bookingRepository,
                    vehicleRepository = vehicleRepository,
                    userRepository = userRepository,
                    authStateViewModel = authStateViewModel
                )
            }
        }
    }
}