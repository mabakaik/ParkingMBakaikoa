package com.lksnext.parkingmbakaikoa.ui.home.screens.profile.addVehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lksnext.parkingmbakaikoa.data.models.Vehicle
import com.lksnext.parkingmbakaikoa.data.models.VehicleType
import com.lksnext.parkingmbakaikoa.data.repository.VehicleRepository
import com.lksnext.parkingmbakaikoa.ui.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddVehicleViewModel(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddVehicleUiState())
    val uiState: StateFlow<AddVehicleUiState> = _uiState.asStateFlow()

    fun onPlateChange(plate: String) {
        _uiState.update {
            it.copy(
                plate = plate,
                plateError = if (plate.isEmpty()) null else validatePlate(plate)
            )
        }
    }

    fun onTypeSelected(type: VehicleType) {
        _uiState.update {
            it.copy(
                selectedType = type,
                typeError = null
            )
        }
    }

    fun addVehicle() {
        val state = _uiState.value

        // Validar todos los campos
        val plateError = validatePlate(state.plate)
        val typeError = if (state.selectedType == null) "Debes seleccionar un tipo de vehículo" else null

        if (plateError != null || typeError != null) {
            _uiState.update {
                it.copy(
                    plateError = plateError,
                    typeError = typeError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val newVehicle = Vehicle(
                plate = state.plate.trim().uppercase(),
                type = state.selectedType!!,
                userId = "1" // Hardcoded por ahora
            )

            val result = vehicleRepository.addVehicle(newVehicle)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isAdded = true) }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        plateError = exception.message ?: "Error al añadir vehículo"
                    )
                }
            }
        }
    }

    private fun validatePlate(plate: String): String? {
        return when {
            plate.isBlank() -> "La matrícula es obligatoria"
            !ValidationUtils.isValidPlate(plate) -> "La matrícula debe tener al menos 3 caracteres"
            else -> null
        }
    }
}

