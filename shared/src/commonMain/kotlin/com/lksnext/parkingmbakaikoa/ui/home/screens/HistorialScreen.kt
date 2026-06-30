package com.lksnext.parkingmbakaikoa.ui.home.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lksnext.parkingmbakaikoa.data.models.Booking
import com.lksnext.parkingmbakaikoa.ui.home.screens.history.HistorialViewModel
import com.lksnext.parkingmbakaikoa.ui.home.screens.myBookings.getBookingStatusBackgroundColor
import com.lksnext.parkingmbakaikoa.ui.home.screens.myBookings.getBookingStatusText
import com.lksnext.parkingmbakaikoa.ui.home.screens.myBookings.getBookingStatusTextColor
import com.lksnext.parkingmbakaikoa.ui.theme.CardShape
import com.lksnext.parkingmbakaikoa.ui.theme.subtitleGreyMedium
import org.jetbrains.compose.resources.stringResource
import parkingmbakaikoa.shared.generated.resources.Res
import parkingmbakaikoa.shared.generated.resources.last30Days
import parkingmbakaikoa.shared.generated.resources.noHistoryBookings
import parkingmbakaikoa.shared.generated.resources.spotName

@Composable
fun HistorialScreen(
    viewModel: HistorialViewModel = HistorialViewModel(),
    onBookingClick: (Booking) -> Unit
) {
    val historyBookings = viewModel.getHistoryBookings()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (historyBookings.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                item {
                    Text(
                        text = stringResource(Res.string.last30Days),
                        style = MaterialTheme.typography.titleSmall,
                        color = subtitleGreyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
                items(historyBookings) { booking ->
                    HistoryBookingCard(
                        booking = booking,
                        onClick = { onBookingClick(booking) }
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = subtitleGreyMedium
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = stringResource(Res.string.noHistoryBookings),
                    style = MaterialTheme.typography.bodyMedium,
                    color = subtitleGreyMedium
                )
            }
        }
    }
}

@Composable
fun HistoryBookingCard(booking: Booking, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 16.dp)
            .clickable(onClick = onClick),
        shape = CardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.spotName, booking.parkingSpot.name),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.size(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = subtitleGreyMedium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = booking.date,
                        style = MaterialTheme.typography.labelMedium,
                        color = subtitleGreyMedium
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = subtitleGreyMedium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${booking.entryTime} - ${booking.exitTime}",
                        style = MaterialTheme.typography.labelMedium,
                        color = subtitleGreyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Badge(
                modifier = Modifier
                    .background(
                        color = getBookingStatusBackgroundColor(booking.status),
                        shape = RoundedCornerShape(6.dp)
                    ),
                containerColor = getBookingStatusBackgroundColor(booking.status)
            ) {
                Text(
                    text = getBookingStatusText(booking.status),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    color = getBookingStatusTextColor(booking.status)
                )
            }
        }
    }
}

