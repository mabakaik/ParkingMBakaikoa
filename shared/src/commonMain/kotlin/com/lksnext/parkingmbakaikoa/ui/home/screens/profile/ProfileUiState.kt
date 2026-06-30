package com.lksnext.parkingmbakaikoa.ui.home.screens.profile

import com.lksnext.parkingmbakaikoa.data.models.User
import com.lksnext.parkingmbakaikoa.data.models.Vehicle

data class ProfileUiState(
    val user: User? = null,
    val vehicles: List<Vehicle> = emptyList(),
    val isDeleteMode: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDeleteDialog: Vehicle? = null
)

