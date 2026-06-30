package com.lksnext.parkingmbakaikoa.ui.home.screens.profile.addVehicle

import com.lksnext.parkingmbakaikoa.data.models.VehicleType

data class AddVehicleUiState(
    val plate: String = "",
    val selectedType: VehicleType? = null,
    val plateError: String? = null,
    val typeError: String? = null,
    val isLoading: Boolean = false,
    val isAdded: Boolean = false
)

