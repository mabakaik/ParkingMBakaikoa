package com.lksnext.parkingmbakaikoa.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.lksnext.parkingmbakaikoa.ui.auth.AuthStateViewModel
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lksnext.parkingmbakaikoa.data.repository.AuthRepository
import com.lksnext.parkingmbakaikoa.ui.home.screens.CalendarioScreen
import com.lksnext.parkingmbakaikoa.ui.home.screens.HistorialScreen
import com.lksnext.parkingmbakaikoa.ui.home.screens.bookingDetail.BookingDetailScreen
import com.lksnext.parkingmbakaikoa.ui.home.screens.myBookings.MyBookingsScreen
import com.lksnext.parkingmbakaikoa.ui.home.screens.PerfilScreen
import com.lksnext.parkingmbakaikoa.ui.home.screens.bookingDetail.BookingDetailViewModel
import com.lksnext.parkingmbakaikoa.ui.home.screens.myBookings.MyBookingsViewModel
import com.lksnext.parkingmbakaikoa.ui.navigation.Routes
import com.lksnext.parkingmbakaikoa.ui.theme.BackgroundLight
import com.lksnext.parkingmbakaikoa.ui.theme.PrimaryColor
import com.lksnext.parkingmbakaikoa.ui.theme.SecondaryColor
import com.lksnext.parkingmbakaikoa.ui.theme.SurfaceLight
import org.jetbrains.compose.resources.stringResource
import parkingmbakaikoa.shared.generated.resources.Res
import parkingmbakaikoa.shared.generated.resources.calendar
import parkingmbakaikoa.shared.generated.resources.history
import parkingmbakaikoa.shared.generated.resources.myBookings
import parkingmbakaikoa.shared.generated.resources.profile

@Composable
fun HomeScreen(authRepository: AuthRepository) {
    val authStateViewModel = viewModel { AuthStateViewModel(authRepository) }
    val homeNavController = rememberNavController()
    val myBookingsViewModel = viewModel { MyBookingsViewModel() }
    val bookingDetailViewModel = viewModel { BookingDetailViewModel() }
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Obtener el título según la ruta actual
    val title = when (currentRoute) {
        Routes.MyBookings.name -> stringResource(Res.string.myBookings)
        Routes.Calendar.name -> stringResource(Res.string.calendar)
        Routes.History.name -> stringResource(Res.string.history)
        Routes.Profile.name -> stringResource(Res.string.profile)
        Routes.BookingDetail.name -> "Detalle de Reserva"
        else -> "Parking App"
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                // Mostrar flecha de back solo en BookingDetail
                if (currentRoute == Routes.BookingDetail.name) {
                    IconButton(
                        onClick = {
                            myBookingsViewModel.clearSelection()
                            homeNavController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            },
            actions = {
                IconButton(
                    onClick = { authStateViewModel.logout() }
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            NavHost(
                navController = homeNavController,
                startDestination = Routes.MyBookings.name
            ) {
                composable(route = Routes.MyBookings.name) {
                    MyBookingsScreen(
                        viewModel = myBookingsViewModel,
                        navController = homeNavController
                    )
                }
                composable(route = Routes.Calendar.name) {
                    CalendarioScreen()
                }
                composable(route = Routes.History.name) {
                    HistorialScreen()
                }
                composable(route = Routes.Profile.name) {
                    PerfilScreen()
                }
                composable(route = Routes.BookingDetail.name) {
                    val selectedBooking = myBookingsViewModel.selectedBooking.value
                    if (selectedBooking != null) {
                        BookingDetailScreen(
                            booking = selectedBooking,
                            onModify = {
                                // TODO: Navegar a pantalla de modificar
                            },
                            viewModel = bookingDetailViewModel
                        )
                    }
                }
            }
        }

        val navItemColors = NavigationBarItemColors(
            selectedIconColor = PrimaryColor,
            selectedTextColor = PrimaryColor,
            selectedIndicatorColor = Color.Transparent,
            unselectedIconColor = SecondaryColor,
            unselectedTextColor = SecondaryColor,
            disabledIconColor = BackgroundLight,
            disabledTextColor = BackgroundLight,
        )

        NavigationBar(
            modifier = Modifier
                .fillMaxWidth(),
            containerColor = SurfaceLight
        ) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) },
                label = { Text(stringResource(Res.string.myBookings)) },
                selected = currentRoute == Routes.MyBookings.name || currentRoute == Routes.BookingDetail.name,
                onClick = {
                    homeNavController.navigate(Routes.MyBookings.name) {
                        popUpTo(Routes.MyBookings.name) { inclusive = true }
                    }
                },
                colors = navItemColors,
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                label = { Text(stringResource(Res.string.calendar)) },
                selected = currentRoute == Routes.Calendar.name,
                onClick = {
                    homeNavController.navigate(Routes.Calendar.name) {
                        popUpTo(Routes.MyBookings.name)
                    }
                },
                colors = navItemColors,
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.History, contentDescription = null) },
                label = { Text(stringResource(Res.string.history)) },
                selected = currentRoute == Routes.History.name,
                onClick = {
                    homeNavController.navigate(Routes.History.name) {
                        popUpTo(Routes.MyBookings.name)
                    }
                },
                colors = navItemColors,
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                label = { Text(stringResource(Res.string.profile)) },
                selected = currentRoute == Routes.Profile.name,
                onClick = {
                    homeNavController.navigate(Routes.Profile.name) {
                        popUpTo(Routes.MyBookings.name)
                    }
                },
                colors = navItemColors,
            )
        }
    }
}

