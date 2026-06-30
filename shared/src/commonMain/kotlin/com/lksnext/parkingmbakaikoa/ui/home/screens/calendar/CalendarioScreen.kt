package com.lksnext.parkingmbakaikoa.ui.home.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lksnext.parkingmbakaikoa.data.models.ParkingSpot
import com.lksnext.parkingmbakaikoa.data.models.VehicleType
import com.lksnext.parkingmbakaikoa.ui.theme.BorderColor
import com.lksnext.parkingmbakaikoa.ui.theme.PrimaryColor
import com.lksnext.parkingmbakaikoa.ui.theme.SuccessColor
import com.lksnext.parkingmbakaikoa.ui.theme.primaryBackground
import com.lksnext.parkingmbakaikoa.ui.theme.subtitleGreyMedium
import org.jetbrains.compose.resources.stringResource
import kotlinx.datetime.DayOfWeek
import parkingmbakaikoa.shared.generated.resources.Res
import parkingmbakaikoa.shared.generated.resources.calendarAvailableSpots
import parkingmbakaikoa.shared.generated.resources.calendarFilterAll
import parkingmbakaikoa.shared.generated.resources.calendarNextPage
import parkingmbakaikoa.shared.generated.resources.calendarNoSpots
import parkingmbakaikoa.shared.generated.resources.calendarPageIndicator
import parkingmbakaikoa.shared.generated.resources.calendarPreviousPage
import parkingmbakaikoa.shared.generated.resources.calendarSpotFree
import parkingmbakaikoa.shared.generated.resources.calendarSpotOccupied
import parkingmbakaikoa.shared.generated.resources.calendarToday
import parkingmbakaikoa.shared.generated.resources.dayFriday
import parkingmbakaikoa.shared.generated.resources.dayMonday
import parkingmbakaikoa.shared.generated.resources.daySaturday
import parkingmbakaikoa.shared.generated.resources.daySunday
import parkingmbakaikoa.shared.generated.resources.dayThursday
import parkingmbakaikoa.shared.generated.resources.dayTuesday
import parkingmbakaikoa.shared.generated.resources.dayWednesday
import parkingmbakaikoa.shared.generated.resources.vehicleTypeCar
import parkingmbakaikoa.shared.generated.resources.vehicleTypeElectric
import parkingmbakaikoa.shared.generated.resources.vehicleTypeMotorbike

@Composable
fun CalendarioScreen(
    viewModel: CalendarioViewModel,
    onSpotSelected: (ParkingSpot) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DaySelector(
            days = uiState.days,
            selectedDate = uiState.selectedDate,
            onDaySelected = viewModel::onDaySelected
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(Res.string.calendarAvailableSpots),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            TypeFilterDropdown(
                selectedType = uiState.selectedType,
                onTypeSelected = viewModel::onTypeFilterSelected
            )
        }


        val pageSpots = viewModel.getCurrentPageSpots()

        if (pageSpots.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.calendarNoSpots),
                    style = MaterialTheme.typography.bodyMedium,
                    color = subtitleGreyMedium,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            SpotsGrid(
                spots = pageSpots,
                onSpotClick = { spot ->
                    viewModel.selectSpot(spot)
                    onSpotSelected(spot)
                },
                modifier = Modifier.weight(1f)
            )
        }

        PaginationControls(
            currentPage = uiState.currentPage,
            pageCount = uiState.pageCount,
            onPrevious = viewModel::onPreviousPage,
            onNext = viewModel::onNextPage
        )
    }
}

@Composable
private fun DaySelector(
    days: List<CalendarDay>,
    selectedDate: kotlinx.datetime.LocalDate?,
    onDaySelected: (kotlinx.datetime.LocalDate) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(days) { day ->
            val isSelected = day.date == selectedDate
            Surface(
                modifier = Modifier
                    .width(54.dp)
                    .clickable { onDaySelected(day.date) },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) PrimaryColor else MaterialTheme.colorScheme.surface,
                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (day.isToday) stringResource(Res.string.calendarToday) else weekDayLabel(day.dayOfWeek),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelected) Color.White else subtitleGreyMedium
                    )
                    Text(
                        text = day.dayNumber.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun TypeFilterDropdown(
    selectedType: VehicleType?,
    onTypeSelected: (VehicleType?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedLabel = when (selectedType) {
        null -> stringResource(Res.string.calendarFilterAll)
        VehicleType.CAR -> stringResource(Res.string.vehicleTypeCar)
        VehicleType.MOTORBIKE -> stringResource(Res.string.vehicleTypeMotorbike)
        VehicleType.ELECTRIC_CAR -> stringResource(Res.string.vehicleTypeElectric)
    }

    val allLabel = stringResource(Res.string.calendarFilterAll)
    val carLabel = stringResource(Res.string.vehicleTypeCar)
    val motorbikeLabel = stringResource(Res.string.vehicleTypeMotorbike)
    val electricLabel = stringResource(Res.string.vehicleTypeElectric)
        Box {
            Surface(
                modifier = Modifier.clickable { expanded = true },
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (selectedType != null) {
                        Icon(
                            imageVector = selectedType.toIcon(),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = PrimaryColor
                        )
                    }
                    Text(
                        text = selectedLabel,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = subtitleGreyMedium
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                FilterMenuItem(
                    label = allLabel,
                    icon = null,
                    onClick = { onTypeSelected(null); expanded = false }
                )
                FilterMenuItem(
                    label = carLabel,
                    icon = Icons.Default.DirectionsCar,
                    onClick = { onTypeSelected(VehicleType.CAR); expanded = false }
                )
                FilterMenuItem(
                    label = motorbikeLabel,
                    icon = Icons.Default.TwoWheeler,
                    onClick = { onTypeSelected(VehicleType.MOTORBIKE); expanded = false }
                )
                FilterMenuItem(
                    label = electricLabel,
                    icon = Icons.Default.ElectricCar,
                    onClick = { onTypeSelected(VehicleType.ELECTRIC_CAR); expanded = false }
                )
            }
        }
}

@Composable
private fun FilterMenuItem(
    label: String,
    icon: ImageVector?,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = PrimaryColor
                    )
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        onClick = onClick,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp
        )
    )
}

@Composable
private fun SpotsGrid(
    spots: List<ParkingSpot>,
    onSpotClick: (ParkingSpot) -> Unit,
    modifier: Modifier = Modifier
) {
    // Cuadrícula de 2 columnas: agrupamos las plazas de dos en dos
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        spots.chunked(2).forEach { rowSpots ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowSpots.forEach { spot ->
                    SpotCell(
                        spot = spot,
                        onClick = { onSpotClick(spot) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Si la fila tiene una sola plaza, rellenamos el espacio restante
                if (rowSpots.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun SpotCell(
    spot: ParkingSpot,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFree = spot.status == "Libre"
    val containerColor = if (isFree) primaryBackground else MaterialTheme.colorScheme.surface
    val contentColor = if (isFree) MaterialTheme.colorScheme.onSurface else subtitleGreyMedium

    Surface(
        modifier = modifier
            .aspectRatio(2.2f)
            .then(if (isFree) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(10.dp),
        color = containerColor,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isFree) PrimaryColor else BorderColor
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = spot.type.toIcon(),
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = if (isFree) PrimaryColor else subtitleGreyMedium
            )
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = spot.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    text = if (isFree) stringResource(Res.string.calendarSpotFree)
                    else stringResource(Res.string.calendarSpotOccupied),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isFree) SuccessColor else subtitleGreyMedium
                )
            }
        }
    }
}

@Composable
private fun PaginationControls(
    currentPage: Int,
    pageCount: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPrevious,
            enabled = currentPage > 0
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(Res.string.calendarPreviousPage),
                tint = if (currentPage > 0) PrimaryColor else BorderColor
            )
        }

        Text(
            text = stringResource(Res.string.calendarPageIndicator, currentPage + 1, pageCount),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        IconButton(
            onClick = onNext,
            enabled = currentPage < pageCount - 1
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(Res.string.calendarNextPage),
                tint = if (currentPage < pageCount - 1) PrimaryColor else BorderColor
            )
        }
    }
}

private fun VehicleType.toIcon(): ImageVector = when (this) {
    VehicleType.CAR -> Icons.Default.DirectionsCar
    VehicleType.MOTORBIKE -> Icons.Default.TwoWheeler
    VehicleType.ELECTRIC_CAR -> Icons.Default.ElectricCar
}

@Composable
private fun weekDayLabel(dayOfWeek: DayOfWeek): String = when (dayOfWeek) {
    DayOfWeek.MONDAY -> stringResource(Res.string.dayMonday)
    DayOfWeek.TUESDAY -> stringResource(Res.string.dayTuesday)
    DayOfWeek.WEDNESDAY -> stringResource(Res.string.dayWednesday)
    DayOfWeek.THURSDAY -> stringResource(Res.string.dayThursday)
    DayOfWeek.FRIDAY -> stringResource(Res.string.dayFriday)
    DayOfWeek.SATURDAY -> stringResource(Res.string.daySaturday)
    DayOfWeek.SUNDAY -> stringResource(Res.string.daySunday)
}





