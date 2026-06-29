package com.lksnext.parkingmbakaikoa.ui.home.screens.createBooking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lksnext.parkingmbakaikoa.ui.theme.PrimaryColor
import com.lksnext.parkingmbakaikoa.ui.theme.subtitleGreyMedium
import org.jetbrains.compose.resources.stringResource
import parkingmbakaikoa.shared.generated.resources.Res
import parkingmbakaikoa.shared.generated.resources.accept
import parkingmbakaikoa.shared.generated.resources.cancel
import parkingmbakaikoa.shared.generated.resources.chooseDate
import parkingmbakaikoa.shared.generated.resources.chooseHour
import parkingmbakaikoa.shared.generated.resources.createBooking
import parkingmbakaikoa.shared.generated.resources.defaultErrorCreatingBookingText
import parkingmbakaikoa.shared.generated.resources.entryTime
import parkingmbakaikoa.shared.generated.resources.errorCreatingBookingTitle
import parkingmbakaikoa.shared.generated.resources.exitTime
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBookingScreen(
    viewModel: CreateBookingViewModel,
    onBookingCreated: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredParkingSpots = viewModel.getFilteredParkingSpots()
    val userVehicles = viewModel.getUserVehicles()
    val coroutineScope = rememberCoroutineScope()

    var vehicleDropdownExpanded by remember { mutableStateOf(false) }
    var showParkingSpotSuggestions by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showEntryTimePicker by remember { mutableStateOf(false) }
    var showExitTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds())
    val entryTimePickerState = rememberTimePickerState(initialHour = 8, initialMinute = 0)
    val exitTimePickerState = rememberTimePickerState(initialHour = 17, initialMinute = 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DatePickerField(
            label = "Fecha",
            value = uiState.date,
            onClick = { showDatePicker = true },
            error = uiState.dateError
        )

        if (showDatePicker) {
            StyledDatePickerDialog(
                onDismiss = { showDatePicker = false },
                onConfirm = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.onDateChanged(millis)
                    }
                    showDatePicker = false
                },
                state = datePickerState,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            TimePickerField(
                label = stringResource(Res.string.entryTime),
                value = uiState.entryTime,
                onClick = { showEntryTimePicker = true },
                error = uiState.entryTimeError,
                modifier = Modifier.weight(1f)
            )

            TimePickerField(
                label = stringResource(Res.string.exitTime),
                value = uiState.exitTime,
                onClick = { showExitTimePicker = true },
                error = uiState.exitTimeError,
                modifier = Modifier.weight(1f)
            )
        }


        if (showEntryTimePicker) {
            StyledTimePickerDialog(
                state = entryTimePickerState,
                onConfirm = {
                    val formattedTime = "${entryTimePickerState.hour.toString().padStart(2, '0')}:${entryTimePickerState.minute.toString().padStart(2, '0')}"
                    viewModel.onEntryTimeChanged(formattedTime)
                    showEntryTimePicker = false
                },
                onDismiss = { showEntryTimePicker = false }
            )
        }

        if (showExitTimePicker) {
            StyledTimePickerDialog(
                state = exitTimePickerState,
                onConfirm = {
                    val formattedTime = "${exitTimePickerState.hour.toString().padStart(2, '0')}:${exitTimePickerState.minute.toString().padStart(2, '0')}"
                    viewModel.onExitTimeChanged(formattedTime)
                    showExitTimePicker = false
                },
                onDismiss = { showExitTimePicker = false }
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Vehículo",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            ExposedDropdownMenuBox(
                expanded = vehicleDropdownExpanded,
                onExpandedChange = { vehicleDropdownExpanded = !vehicleDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = uiState.selectedVehicle?.plate ?: "",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Selecciona un vehículo") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = subtitleGreyMedium
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = vehicleDropdownExpanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                    isError = uiState.vehicleError != null
                )

                ExposedDropdownMenu(
                    expanded = vehicleDropdownExpanded,
                    onDismissRequest = { vehicleDropdownExpanded = false }
                ) {
                    userVehicles.forEach { vehicle ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = vehicle.plate,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = vehicle.type.name,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = subtitleGreyMedium
                                    )
                                }
                            },
                            onClick = {
                                viewModel.onVehicleSelected(vehicle)
                                vehicleDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            if (uiState.vehicleError != null) {
                Text(
                    text = uiState.vehicleError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Plaza de Parking",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = uiState.parkingSpotSearch,
                onValueChange = {
                    viewModel.onParkingSpotSearchChanged(it)
                    showParkingSpotSuggestions = true
                },
                placeholder = { Text("Buscar plaza (ej: A01, B15...)") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocalParking,
                        contentDescription = null,
                        tint = subtitleGreyMedium
                    )
                },
                trailingIcon = {
                    if (uiState.parkingSpotSearch.isNotEmpty()) {
                        IconButton(onClick = {
                            viewModel.clearParkingSpotSelection()
                            showParkingSpotSuggestions = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = subtitleGreyMedium
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(),
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.parkingSpotError != null
            )

            if (showParkingSpotSuggestions && filteredParkingSpots.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    LazyColumn {
                        items(filteredParkingSpots) { spot ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.onParkingSpotSelected(spot)
                                        showParkingSpotSuggestions = false
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Surface(
                                        modifier = Modifier.size(40.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        color = PrimaryColor.copy(alpha = 0.1f)
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = spot.name,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = PrimaryColor
                                            )
                                        }
                                    }
                                    Column {
                                        Text(
                                            text = spot.name,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "Disponible",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = subtitleGreyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (uiState.parkingSpotError != null) {
                Text(
                    text = uiState.parkingSpotError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.validateAndCreateBooking()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
        ) {
            Text(stringResource(Res.string.createBooking))
        }
    }

    if (uiState.isLoading) {
        LoadingDialog()
    }

    if (uiState.showSuccessDialog) {
        SuccessDialog(
            onDismiss = {
                viewModel.dismissSuccess()
                viewModel.resetForm()
                onBookingCreated()
            }
        )
    }

    if (uiState.errorCreatingBooking) {
        ErrorDialog(
            title = stringResource(Res.string.errorCreatingBookingTitle),
            text = stringResource(Res.string.defaultErrorCreatingBookingText),
            onDismiss = { viewModel.dismissError() }
        )
    }
}

@Composable
private fun DatePickerField(
    label: String,
    value: String,
    onClick: () -> Unit,
    error: String? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("DD/MM/AAAA") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = subtitleGreyMedium
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLeadingIconColor = subtitleGreyMedium
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = error != null,
                enabled = false
            )
        }

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun TimePickerField(
    label: String,
    value: String,
    onClick: () -> Unit,
    error: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Box(
            modifier = Modifier.clickable(onClick = onClick)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("HH:MM") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = subtitleGreyMedium
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLeadingIconColor = subtitleGreyMedium
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = error != null,
                enabled = false
            )
        }

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StyledTimePickerDialog(
    title: String = stringResource(Res.string.chooseHour),
    state: TimePickerState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    TimePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = PrimaryColor)
            ) {
                Text(stringResource(Res.string.accept))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = PrimaryColor)
            ) {
                Text(stringResource(Res.string.cancel))
            }
        },
        title = {Text(title)}
    ) {
        TimePicker(state = state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StyledDatePickerDialog(
    title: String = stringResource(Res.string.chooseDate),
    state: DatePickerState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = PrimaryColor)
            ) {
                Text(
                    stringResource(Res.string.accept)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = PrimaryColor)
            ) {
                Text(
                    stringResource(Res.string.cancel)
                )
            }
        },
    ) {
        DatePicker(
            title = {Text(title)},
            state = state
        )
    }
}

@Composable
private fun LoadingDialog() {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = { },
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator(
                    color = PrimaryColor,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "Cargando...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}

@Composable
private fun ErrorDialog(
    onDismiss: () -> Unit,
    title: String,
    text: String,
    confirmButtonText: String = stringResource(Res.string.accept)
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
            ) {
                Text(confirmButtonText)
            }
        }
    )
}

@Composable
private fun SuccessDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = PrimaryColor,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = "Reserva creada",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = "La reserva se creó correctamente.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
            ) {
                Text("Aceptar")
            }
        }
    )
}


