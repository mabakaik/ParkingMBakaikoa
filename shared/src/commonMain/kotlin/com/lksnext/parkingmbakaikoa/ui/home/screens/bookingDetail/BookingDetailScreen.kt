package com.lksnext.parkingmbakaikoa.ui.home.screens.bookingDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lksnext.parkingmbakaikoa.data.mock.MockData
import com.lksnext.parkingmbakaikoa.data.models.Booking
import com.lksnext.parkingmbakaikoa.data.models.BookingStatus
import com.lksnext.parkingmbakaikoa.ui.home.screens.myBookings.MyBookingsViewModel
import com.lksnext.parkingmbakaikoa.ui.home.screens.myBookings.getBookingStatusBackgroundColor
import com.lksnext.parkingmbakaikoa.ui.home.screens.myBookings.getBookingStatusText
import com.lksnext.parkingmbakaikoa.ui.home.screens.myBookings.getBookingStatusTextColor
import com.lksnext.parkingmbakaikoa.ui.theme.CardShape
import com.lksnext.parkingmbakaikoa.ui.theme.PrimaryColor
import com.lksnext.parkingmbakaikoa.ui.theme.subtitleGreyMedium

@Composable
fun BookingDetailScreen(
    booking: Booking,
    onModify: () -> Unit,
    viewModel: BookingDetailViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = CardShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Plaza ${booking.parkingSpot.spaceName}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

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
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = getBookingStatusTextColor(booking.status)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))

                DetailRow(
                    icon = Icons.Default.CalendarToday,
                    label = "Fecha",
                    value = booking.date
                )

                DetailRow(
                    icon = Icons.Default.Timer,
                    label = "Hora",
                    value = booking.entryTime + " - " + booking.exitTime
                )

                DetailRow(
                    icon = Icons.Default.DirectionsCar,
                    label = "Vehículo",
                    value = booking.vehicle.plate
                )
            }
        }

        // Horarios reales
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = CardShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Registro de horarios",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                DetailRow(
                    icon = Icons.Default.Login,
                    label = "Entrada",
                    value = booking.actualEntryTime ?: "No registrada",
                    valueColor = if (booking.actualEntryTime != null)
                        MaterialTheme.colorScheme.onSurface
                    else
                        subtitleGreyMedium
            )

            DetailRow(
                icon = Icons.AutoMirrored.Filled.Logout,
                label = "Salida",
                    value = booking.actualExitTime ?: "No registrada",
                    valueColor = if (booking.actualExitTime != null)
                        MaterialTheme.colorScheme.onSurface
                    else
                        subtitleGreyMedium
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Botón Registrar Entrada (solo si no hay entrada registrada)
            if (booking.actualEntryTime == null) {
                Button(
                    onClick = { viewModel.registerEntry(booking) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Login,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Registrar Entrada")
                }
            }

            if (booking.actualEntryTime != null && booking.actualExitTime == null) {
                Button(
                    onClick = { viewModel.registerEntry(booking) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Registrar Salida")
                }
            }

            if (booking.status == BookingStatus.CONFIRMADA) {
                OutlinedButton(
                    onClick = onModify,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Modificar Reserva")
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = subtitleGreyMedium
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = subtitleGreyMedium
            )
        }

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor
        )
    }
}

@Preview
@Composable
fun BookingDetailScreenPreview() {
    BookingDetailScreen(
        booking = MockData.mockBooking1,
        onModify = {},
        viewModel = viewModel { BookingDetailViewModel() }
    )
}

@Preview
@Composable
fun BookingDetailScreenInProgressPreview() {
    BookingDetailScreen(
        booking = MockData.mockBooking2,
        onModify = {},
        viewModel = viewModel { BookingDetailViewModel() }
    )
}

@Preview
@Composable
fun BookingDetailScreenCompletedPreview() {
    BookingDetailScreen(
        booking = MockData.mockBooking5,
        onModify = {},
        viewModel = viewModel { BookingDetailViewModel() }
    )
}

