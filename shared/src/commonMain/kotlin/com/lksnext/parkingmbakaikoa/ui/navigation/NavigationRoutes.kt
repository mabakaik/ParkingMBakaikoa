package com.lksnext.parkingmbakaikoa.ui.navigation

sealed class NavigationRoute {
    data object Login : NavigationRoute()
    data object Home : NavigationRoute()
    data object MyBookings : NavigationRoute()
    data object Calendar : NavigationRoute()
    data object History : NavigationRoute()
    data object Profile : NavigationRoute()
}

sealed class HomeTab {
    data object MyBookings : HomeTab()
    data object Calendar : HomeTab()
    data object History : HomeTab()
    data object Profile : HomeTab()
}

