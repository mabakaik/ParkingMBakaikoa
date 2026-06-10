package com.lksnext.parkingmbakaikoa.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.lksnext.parkingmbakaikoa.ui.home.screens.CalendarioScreen
import com.lksnext.parkingmbakaikoa.ui.home.screens.HistorialScreen
import com.lksnext.parkingmbakaikoa.ui.home.screens.PerfilScreen
import com.lksnext.parkingmbakaikoa.ui.home.screens.ReservasScreen
import com.lksnext.parkingmbakaikoa.ui.navigation.HomeTab
import com.lksnext.parkingmbakaikoa.ui.theme.BackgroundLight
import com.lksnext.parkingmbakaikoa.ui.theme.ParkingAppTheme
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
fun HomeScreen() {
    val selectedTab = remember { mutableStateOf<HomeTab>(HomeTab.MyBookings) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (selectedTab.value) {
                HomeTab.MyBookings -> ReservasScreen()
                HomeTab.Calendar -> CalendarioScreen()
                HomeTab.History -> HistorialScreen()
                HomeTab.Profile -> PerfilScreen()
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
                selected = selectedTab.value == HomeTab.MyBookings,
                onClick = { selectedTab.value = HomeTab.MyBookings },
                colors = navItemColors,
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                label = { Text(stringResource(Res.string.calendar)) },
                selected = selectedTab.value == HomeTab.Calendar,
                onClick = { selectedTab.value = HomeTab.Calendar },
                colors = navItemColors,
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.History, contentDescription = null) },
                label = { Text(stringResource(Res.string.history)) },
                selected = selectedTab.value == HomeTab.History,
                onClick = { selectedTab.value = HomeTab.History },
                colors = navItemColors,
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                label = { Text(stringResource(Res.string.profile)) },
                selected = selectedTab.value == HomeTab.Profile,
                onClick = { selectedTab.value = HomeTab.Profile },
                colors = navItemColors,
            )
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview
@Composable
fun HomeScreenPreview() {
    ParkingAppTheme {
        HomeScreen()
    }
}

