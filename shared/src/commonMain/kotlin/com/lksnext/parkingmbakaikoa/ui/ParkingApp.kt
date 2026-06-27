package com.lksnext.parkingmbakaikoa.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepository
import com.lksnext.parkingmbakaikoa.ui.home.HomeScreen
import com.lksnext.parkingmbakaikoa.ui.login.LoginScreen
import com.lksnext.parkingmbakaikoa.ui.navigation.Routes
import com.lksnext.parkingmbakaikoa.ui.register.RegisterScreen
import com.lksnext.parkingmbakaikoa.ui.resetPassword.ResetPasswordScreen
import com.lksnext.parkingmbakaikoa.ui.theme.ParkingAppTheme

@Composable
fun ParkingApp(authRepository: AuthRepository){
    val navController: NavHostController = rememberNavController()

    ParkingAppTheme {
        NavHost(
            navController = navController,
            startDestination = Routes.Login.name,
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
                    authRepository = authRepository
                )
            }
        }
    }
}