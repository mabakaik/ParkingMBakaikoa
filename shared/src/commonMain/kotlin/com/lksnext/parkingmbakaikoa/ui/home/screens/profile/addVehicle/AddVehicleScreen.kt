package com.lksnext.parkingmbakaikoa.ui.home.screens.profile.addVehicle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lksnext.parkingmbakaikoa.data.models.VehicleType
import com.lksnext.parkingmbakaikoa.ui.theme.PrimaryColor
import org.jetbrains.compose.resources.stringResource
import parkingmbakaikoa.shared.generated.resources.Res
import parkingmbakaikoa.shared.generated.resources.addVehicle
import parkingmbakaikoa.shared.generated.resources.platePlaceholder
import parkingmbakaikoa.shared.generated.resources.selectVehicleType
import parkingmbakaikoa.shared.generated.resources.vehiclePlate
import parkingmbakaikoa.shared.generated.resources.vehicleType
import parkingmbakaikoa.shared.generated.resources.vehicleTypeCar
import parkingmbakaikoa.shared.generated.resources.vehicleTypeElectric
import parkingmbakaikoa.shared.generated.resources.vehicleTypeMotorbike

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVehicleScreen(
    viewModel: AddVehicleViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var typeDropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isAdded) {
        if (uiState.isAdded) {
            onBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Campo Matrícula
        OutlinedTextField(
            value = uiState.plate,
            onValueChange = { viewModel.onPlateChange(it) },
            label = { Text(stringResource(Res.string.vehiclePlate)) },
            placeholder = { Text(stringResource(Res.string.platePlaceholder)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading,
            isError = uiState.plateError != null,
            supportingText = {
                uiState.plateError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryColor,
                focusedLabelColor = PrimaryColor,
                cursorColor = PrimaryColor
            )
        )

        ExposedDropdownMenuBox(
            expanded = typeDropdownExpanded,
            onExpandedChange = {
                if (!uiState.isLoading) {
                    typeDropdownExpanded = !typeDropdownExpanded
                }
            }
        ) {
            OutlinedTextField(
                value = uiState.selectedType?.let { getVehicleTypeDisplayName(it) } ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(Res.string.vehicleType)) },
                placeholder = { Text(stringResource(Res.string.selectVehicleType)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeDropdownExpanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                enabled = !uiState.isLoading,
                isError = uiState.typeError != null,
                supportingText = {
                    uiState.typeError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryColor,
                    focusedLabelColor = PrimaryColor
                )
            )

            ExposedDropdownMenu(
                expanded = typeDropdownExpanded,
                onDismissRequest = { typeDropdownExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.vehicleTypeCar)) },
                    onClick = {
                        viewModel.onTypeSelected(VehicleType.CAR)
                        typeDropdownExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.vehicleTypeMotorbike)) },
                    onClick = {
                        viewModel.onTypeSelected(VehicleType.MOTORBIKE)
                        typeDropdownExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.vehicleTypeElectric)) },
                    onClick = {
                        viewModel.onTypeSelected(VehicleType.ELECTRIC_CAR)
                        typeDropdownExpanded = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.addVehicle() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading &&
                     uiState.plate.isNotBlank() &&
                     uiState.selectedType != null,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryColor
            )
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(stringResource(Res.string.addVehicle))
            }
        }
    }
}

@Composable
private fun getVehicleTypeDisplayName(type: VehicleType): String {
    return when (type) {
        VehicleType.CAR -> stringResource(Res.string.vehicleTypeCar)
        VehicleType.MOTORBIKE -> stringResource(Res.string.vehicleTypeMotorbike)
        VehicleType.ELECTRIC_CAR -> stringResource(Res.string.vehicleTypeElectric)
    }
}

