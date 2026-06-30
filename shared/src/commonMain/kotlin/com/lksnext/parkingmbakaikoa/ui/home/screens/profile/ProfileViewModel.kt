package com.lksnext.parkingmbakaikoa.ui.home.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lksnext.parkingmbakaikoa.data.models.Vehicle
import com.lksnext.parkingmbakaikoa.data.repository.UserRepository
import com.lksnext.parkingmbakaikoa.data.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val vehicleRepository: VehicleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    fun loadUserData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Cargar usuario
            val userResult = userRepository.getCurrentUser()
            userResult.onSuccess { user ->
                _uiState.update { it.copy(user = user) }
            }.onFailure { exception ->
                _uiState.update { it.copy(error = exception.message) }
            }

            // Cargar vehículos
            val vehiclesResult = vehicleRepository.getUserVehicles()
            vehiclesResult.onSuccess { vehicles ->
                _uiState.update { it.copy(vehicles = vehicles) }
            }.onFailure { exception ->
                _uiState.update { it.copy(error = exception.message) }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun toggleDeleteMode() {
        _uiState.update { it.copy(isDeleteMode = !it.isDeleteMode) }
    }

    fun showDeleteConfirmation(vehicle: Vehicle) {
        _uiState.update { it.copy(showDeleteDialog = vehicle) }
    }

    fun dismissDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = null) }
    }

    fun deleteVehicle(vehicleId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = vehicleRepository.deleteVehicle(vehicleId)
            result.onSuccess {
                // Recargar vehículos después de eliminar
                loadUserData()
                dismissDeleteDialog()
                // Desactivar modo eliminación si no quedan vehículos
                if (_uiState.value.vehicles.size <= 1) {
                    _uiState.update { it.copy(isDeleteMode = false) }
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        error = exception.message,
                        isLoading = false
                    )
                }
            }
        }
    }
}

