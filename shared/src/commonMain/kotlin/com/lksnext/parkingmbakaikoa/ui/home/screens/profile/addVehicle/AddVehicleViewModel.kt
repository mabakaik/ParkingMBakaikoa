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
import org.jetbrains.compose.resources.getString
import parkingmbakaikoa.shared.generated.resources.Res
import parkingmbakaikoa.shared.generated.resources.addVehicleError
import parkingmbakaikoa.shared.generated.resources.plateInvalid
import parkingmbakaikoa.shared.generated.resources.plateRequired
import parkingmbakaikoa.shared.generated.resources.vehicleTypeRequired

class AddVehicleViewModel(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddVehicleUiState())
    val uiState: StateFlow<AddVehicleUiState> = _uiState.asStateFlow()

    fun onPlateChange(plate: String) {
        viewModelScope.launch {
            val plateError = if (plate.isEmpty()) null else validatePlate(plate)
            _uiState.update {
                it.copy(
                    plate = plate,
                    plateError = plateError
                )
            }
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
        viewModelScope.launch {
            val state = _uiState.value

            // Validar todos los campos
            val plateError = validatePlate(state.plate)
            val typeError = if (state.selectedType == null) getString(Res.string.vehicleTypeRequired) else null

            if (plateError != null || typeError != null) {
                _uiState.update {
                    it.copy(
                        plateError = plateError,
                        typeError = typeError
                    )
                }
                return@launch
            }

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
                        plateError = exception.message ?: getString(Res.string.addVehicleError)
                    )
                }
            }
        }
    }

    private suspend fun validatePlate(plate: String): String? {
        return when {
            plate.isBlank() -> getString(Res.string.plateRequired)
            !ValidationUtils.isValidPlate(plate) -> getString(Res.string.plateInvalid)
            else -> null
        }
    }
}

