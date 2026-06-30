package com.lksnext.parkingmbakaikoa.ui.home.screens.myBookings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lksnext.parkingmbakaikoa.data.models.Booking
import com.lksnext.parkingmbakaikoa.data.models.BookingStatus
import com.lksnext.parkingmbakaikoa.ui.navigation.Routes
import com.lksnext.parkingmbakaikoa.ui.theme.BookingCancelledBackground
import com.lksnext.parkingmbakaikoa.ui.theme.BookingCancelledText
import com.lksnext.parkingmbakaikoa.ui.theme.BookingCompletedBackground
import com.lksnext.parkingmbakaikoa.ui.theme.BookingCompletedText
import com.lksnext.parkingmbakaikoa.ui.theme.BookingConfirmedBackground
import com.lksnext.parkingmbakaikoa.ui.theme.BookingConfirmedText
import com.lksnext.parkingmbakaikoa.ui.theme.BookingInProgressBackground
import com.lksnext.parkingmbakaikoa.ui.theme.BookingInProgressText
import com.lksnext.parkingmbakaikoa.ui.theme.CardShape
import com.lksnext.parkingmbakaikoa.ui.theme.primaryBackground
import com.lksnext.parkingmbakaikoa.ui.theme.subtitleGreyMedium
import org.jetbrains.compose.resources.stringResource
import parkingmbakaikoa.shared.generated.resources.Res
import parkingmbakaikoa.shared.generated.resources.bookingStatusCancelled
import parkingmbakaikoa.shared.generated.resources.bookingStatusConfirmed
import parkingmbakaikoa.shared.generated.resources.bookingStatusFinished
import parkingmbakaikoa.shared.generated.resources.bookingStatusInProgress
import parkingmbakaikoa.shared.generated.resources.createBookingContentDescription
import parkingmbakaikoa.shared.generated.resources.noActiveBookings
import parkingmbakaikoa.shared.generated.resources.spotName
import parkingmbakaikoa.shared.generated.resources.vehicleType
import kotlin.String

@Composable
fun MyBookingsScreen(
    viewModel: MyBookingsViewModel = MyBookingsViewModel(),
    navController: NavHostController
) {
    val activeBookings = viewModel.getActiveBookings()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (activeBookings.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 16.dp),

                ) {
                    items(activeBookings) { booking ->
                        BookingCard(
                            booking = booking,
                            onClick = {
                                viewModel.selectBooking(booking)
                                navController.navigate(Routes.BookingDetail.name)
                            }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.noActiveBookings),
                        style = MaterialTheme.typography.bodyMedium,
                        color = subtitleGreyMedium
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(Routes.CreateBooking.name) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = primaryBackground
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(Res.string.createBookingContentDescription),
                tint = Color.Black
            )
        }
    }
}

@Composable
fun BookingCard(booking: Booking, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = onClick),
        shape = CardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape,
                    color = primaryBackground
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = stringResource(Res.string.vehicleType),
                            modifier = Modifier.size(28.dp),
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = stringResource(Res.string.spotName, booking.parkingSpot.name),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Badge(
                    modifier = Modifier
                        .background(
                            color = getBookingStatusBackgroundColor(booking.status),
                            shape = RoundedCornerShape(6.dp)
                        ),
                    containerColor = getBookingStatusBackgroundColor(booking.status)
                ){
                    Text(
                        text = getBookingStatusText(booking.status),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        color = getBookingStatusTextColor(booking.status)
                    )
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            BookingInfo(
                icon = Icons.Default.Timer,
                text = booking.entryTime + " - " + booking.exitTime
            )

            Spacer(modifier = Modifier.size(8.dp))

            BookingInfo(
                icon = Icons.Default.CalendarToday,
                text = booking.date
            )

            Spacer(modifier = Modifier.size(8.dp))

            BookingInfo(
                icon = Icons.Default.DirectionsCar,
                text = booking.vehicle.plate
            )
        }
    }
}

@Composable
fun getBookingStatusText(status: BookingStatus): String {
    return when (status) {
        BookingStatus.EN_CURSO -> stringResource(Res.string.bookingStatusInProgress)
        BookingStatus.CANCELADA -> stringResource(Res.string.bookingStatusCancelled)
        BookingStatus.CONFIRMADA -> stringResource(Res.string.bookingStatusConfirmed)
        BookingStatus.TERMINADA -> stringResource(Res.string.bookingStatusFinished)
    }
}

@Composable
fun getBookingStatusBackgroundColor(status: BookingStatus): Color {
    return when (status) {
        BookingStatus.EN_CURSO -> BookingInProgressBackground
        BookingStatus.CANCELADA -> BookingCancelledBackground
        BookingStatus.CONFIRMADA -> BookingConfirmedBackground
        BookingStatus.TERMINADA -> BookingCompletedBackground
    }
}

@Composable
fun getBookingStatusTextColor(status: BookingStatus): Color {
    return when (status) {
        BookingStatus.EN_CURSO -> BookingInProgressText
        BookingStatus.CANCELADA -> BookingCancelledText
        BookingStatus.CONFIRMADA -> BookingConfirmedText
        BookingStatus.TERMINADA -> BookingCompletedText
    }
}

@Composable
fun BookingInfo(icon: ImageVector, text: String){
    Row{
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(16.dp),
            tint = subtitleGreyMedium
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text  = text ,
            style = MaterialTheme.typography.labelMedium,
            color = subtitleGreyMedium
        )
    }
}
