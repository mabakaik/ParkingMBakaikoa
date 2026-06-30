package com.lksnext.parkingmbakaikoa.ui.home.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lksnext.parkingmbakaikoa.data.models.User
import com.lksnext.parkingmbakaikoa.data.models.Vehicle
import com.lksnext.parkingmbakaikoa.data.models.VehicleType
import com.lksnext.parkingmbakaikoa.ui.auth.AuthStateViewModel
import com.lksnext.parkingmbakaikoa.ui.navigation.Routes
import com.lksnext.parkingmbakaikoa.ui.theme.CardShape
import com.lksnext.parkingmbakaikoa.ui.theme.PrimaryColor
import com.lksnext.parkingmbakaikoa.ui.theme.subtitleGreyMedium
import org.jetbrains.compose.resources.stringResource
import parkingmbakaikoa.shared.generated.resources.Res
import parkingmbakaikoa.shared.generated.resources.addVehicle
import parkingmbakaikoa.shared.generated.resources.cancel
import parkingmbakaikoa.shared.generated.resources.delete
import parkingmbakaikoa.shared.generated.resources.deleteVehicleConfirm
import parkingmbakaikoa.shared.generated.resources.deleteVehicleMessage
import parkingmbakaikoa.shared.generated.resources.logout
import parkingmbakaikoa.shared.generated.resources.myVehicles
import parkingmbakaikoa.shared.generated.resources.noVehicles
import parkingmbakaikoa.shared.generated.resources.personalInfo
import parkingmbakaikoa.shared.generated.resources.vehicleTypeCar
import parkingmbakaikoa.shared.generated.resources.vehicleTypeElectric
import parkingmbakaikoa.shared.generated.resources.vehicleTypeMotorbike

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    authStateViewModel: AuthStateViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryColor)
            }
        } else {
            PersonalInfoCard(
                user = uiState.user,
                onEditClick = {
                    navController.navigate(Routes.EditProfile.name)
                }
            )

            VehiclesCard(
                vehicles = uiState.vehicles,
                isDeleteMode = uiState.isDeleteMode,
                onToggleDeleteMode = { viewModel.toggleDeleteMode() },
                onDeleteClick = { vehicle -> viewModel.showDeleteConfirmation(vehicle) },
                onAddClick = {
                    navController.navigate(Routes.AddVehicle.name)
                }
            )

            Button(
                onClick = {
                    authStateViewModel.logout()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(stringResource(Res.string.logout))
            }
        }
    }

    uiState.showDeleteDialog?.let { vehicle ->
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeleteDialog() },
            title = { Text(stringResource(Res.string.deleteVehicleConfirm)) },
            text = {
                Text(stringResource(Res.string.deleteVehicleMessage) + "\n${vehicle.plate}")
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.deleteVehicle(vehicle.id) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(Res.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDeleteDialog() }) {
                    Text(stringResource(Res.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun PersonalInfoCard(
    user: User?,
    onEditClick: () -> Unit
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
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.personalInfo),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar perfil",
                        tint = PrimaryColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            user?.let {
                InfoRow(
                    label = "Nombre",
                    value = "${it.firstName} ${it.lastName}"
                )
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(
                    label = "Email",
                    value = it.email
                )
            }
        }
    }
}

@Composable
private fun VehiclesCard(
    vehicles: List<Vehicle>,
    isDeleteMode: Boolean,
    onToggleDeleteMode: () -> Unit,
    onDeleteClick: (Vehicle) -> Unit,
    onAddClick: () -> Unit
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
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.myVehicles),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (vehicles.isNotEmpty()) {
                    TextButton(
                        onClick = onToggleDeleteMode,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            imageVector = if (isDeleteMode) Icons.Default.Close else Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (vehicles.isEmpty()) {
                Text(
                    text = stringResource(Res.string.noVehicles),
                    style = MaterialTheme.typography.bodyMedium,
                    color = subtitleGreyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(vehicles) { vehicle ->
                        VehicleItem(
                            vehicle = vehicle,
                            showDeleteButton = isDeleteMode,
                            onDeleteClick = { onDeleteClick(vehicle) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Card para añadir vehículo
            AddVehicleCard(onClick = onAddClick)
        }
    }
}

@Composable
private fun VehicleItem(
    vehicle: Vehicle,
    showDeleteButton: Boolean,
    onDeleteClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = PrimaryColor.copy(alpha = 0.1f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = getVehicleIcon(vehicle.type),
                            contentDescription = null,
                            tint = PrimaryColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Column {
                    Text(
                        text = vehicle.plate,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = getVehicleTypeName(vehicle.type),
                        style = MaterialTheme.typography.bodySmall,
                        color = subtitleGreyMedium
                    )
                }
            }

            if (showDeleteButton) {
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun AddVehicleCard(onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        color = PrimaryColor.copy(alpha = 0.1f),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = PrimaryColor.copy(alpha = 0.2f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = PrimaryColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = stringResource(Res.string.addVehicle),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = PrimaryColor
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = subtitleGreyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun getVehicleIcon(type: VehicleType): ImageVector {
    return when (type) {
        VehicleType.CAR -> Icons.Default.DirectionsCar
        VehicleType.MOTORBIKE -> Icons.Default.TwoWheeler
        VehicleType.ELECTRIC_CAR -> Icons.Default.ElectricCar
    }
}

@Composable
private fun getVehicleTypeName(type: VehicleType): String {
    return when (type) {
        VehicleType.CAR -> stringResource(Res.string.vehicleTypeCar)
        VehicleType.MOTORBIKE -> stringResource(Res.string.vehicleTypeMotorbike)
        VehicleType.ELECTRIC_CAR -> stringResource(Res.string.vehicleTypeElectric)
    }
}


