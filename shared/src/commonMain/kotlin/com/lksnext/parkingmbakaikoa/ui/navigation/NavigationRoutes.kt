package com.lksnext.parkingmbakaikoa.ui.navigation

sealed class NavigationRoute {
    data object Login : NavigationRoute()
    data object Home : NavigationRoute()
    data object Reservas : NavigationRoute()
    data object Calendario : NavigationRoute()
    data object Historial : NavigationRoute()
    data object Perfil : NavigationRoute()
}

sealed class HomeTab {
    data object MyBookings : HomeTab()
    data object Calendar : HomeTab()
    data object History : HomeTab()
    data object Profile : HomeTab()
}

