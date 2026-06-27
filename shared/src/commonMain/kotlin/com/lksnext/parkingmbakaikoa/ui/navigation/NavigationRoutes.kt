package com.lksnext.parkingmbakaikoa.ui.navigation

import org.jetbrains.compose.resources.StringResource
import parkingmbakaikoa.shared.generated.resources.Res
import parkingmbakaikoa.shared.generated.resources.route_calendar
import parkingmbakaikoa.shared.generated.resources.route_history
import parkingmbakaikoa.shared.generated.resources.route_home
import parkingmbakaikoa.shared.generated.resources.route_login
import parkingmbakaikoa.shared.generated.resources.route_my_bookings
import parkingmbakaikoa.shared.generated.resources.route_profile
import parkingmbakaikoa.shared.generated.resources.route_register
import parkingmbakaikoa.shared.generated.resources.route_reset_password

enum class Routes(val title: StringResource) {
    Home(title = Res.string.route_home),
    Login(title = Res.string.route_login),
    Register(title = Res.string.route_register),
    ResetPassword(title = Res.string.route_reset_password),
    MyBookings(title = Res.string.route_my_bookings),
    Calendar(title = Res.string.route_calendar),
    History(title = Res.string.route_history),
    Profile(title = Res.string.route_profile),
    BookingDetail(title = Res.string.route_my_bookings),
}

