package com.lksnext.parkingmbakaikoa.ui.home.screens.createBooking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lksnext.parkingmbakaikoa.data.mock.MockData
import com.lksnext.parkingmbakaikoa.data.models.Booking
import com.lksnext.parkingmbakaikoa.data.models.BookingStatus
import com.lksnext.parkingmbakaikoa.data.models.ParkingSpot
import com.lksnext.parkingmbakaikoa.data.models.Vehicle
import com.lksnext.parkingmbakaikoa.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.coroutines.resume
import kotlin.time.Clock
import kotlin.time.Instant

data class CreateBookingState(
    val date: String = "",
    val entryTime: String = "",
    val exitTime: String = "",
    val selectedVehicle: Vehicle? = null,
    val parkingSpotSearch: String = "",
    val selectedParkingSpot: ParkingSpot? = null,
    val dateError: String? = null,
    val entryTimeError: String? = null,
    val exitTimeError: String? = null,
    val vehicleError: String? = null,
    val parkingSpotError: String? = null,
    val isLoading: Boolean = false,
    val errorCreatingBooking: Boolean = false,
    val showSuccessDialog: Boolean = false
)

class CreateBookingViewModel(private val bookingRepository: BookingRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateBookingState())
    val uiState: StateFlow<CreateBookingState> = _uiState.asStateFlow()

    private val userVehicles = MockData.mockVehicles

    private val availableParkingSpots = listOf(
        ParkingSpot("A01", "A01", "Libre"),
        ParkingSpot("A02", "A02", "Libre"),
        ParkingSpot("A15", "A15", "Libre"),
        ParkingSpot("B05", "B05", "Libre"),
        ParkingSpot("B12", "B12", "Libre"),
        ParkingSpot("B22", "B22", "Libre"),
        ParkingSpot("C03", "C03", "Libre"),
        ParkingSpot("C05", "C05", "Libre"),
        ParkingSpot("C18", "C18", "Libre"),
        ParkingSpot("D04", "D04", "Libre"),
        ParkingSpot("D12", "D12", "Libre"),
        ParkingSpot("E08", "E08", "Libre"),
        ParkingSpot("E15", "E15", "Libre"),
    )

    init{
        onDateChanged(Clock.System.now().toEpochMilliseconds())
    }

    fun getUserVehicles(): List<Vehicle> = userVehicles

    fun getFilteredParkingSpots(): List<ParkingSpot> {
        val search = _uiState.value.parkingSpotSearch
        return if (search.isEmpty()) {
            emptyList()
        } else {
            availableParkingSpots.filter {
                it.name.contains(search, ignoreCase = true)
            }
        }
    }

    fun onDateChanged(dateInMillis: Long) {
        val instant = Instant.fromEpochMilliseconds(dateInMillis)
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val monthNumber = (localDate.month.ordinal + 1)
        val formattedDate = "${localDate.day.toString().padStart(2, '0')}/" +
                "${monthNumber.toString().padStart(2, '0')}/" +
                "${localDate.year}"

        _uiState.value = _uiState.value.copy(date = formattedDate, dateError = null)
    }

    fun onEntryTimeChanged(time: String) {
        _uiState.value = _uiState.value.copy(entryTime = time, entryTimeError = null)
    }

    fun onExitTimeChanged(time: String) {
        _uiState.value = _uiState.value.copy(exitTime = time, exitTimeError = null)
    }

    fun onVehicleSelected(vehicle: Vehicle) {
        _uiState.value = _uiState.value.copy(selectedVehicle = vehicle, vehicleError = null)
    }

    fun onParkingSpotSearchChanged(search: String) {
        _uiState.value = _uiState.value.copy(
            parkingSpotSearch = search,
            parkingSpotError = null
        )
    }

    fun onParkingSpotSelected(parkingSpot: ParkingSpot) {
        _uiState.value = _uiState.value.copy(
            selectedParkingSpot = parkingSpot,
            parkingSpotSearch = parkingSpot.name,
            parkingSpotError = null
        )
    }

    fun clearParkingSpotSelection() {
        _uiState.value = _uiState.value.copy(
            selectedParkingSpot = null,
            parkingSpotSearch = ""
        )
    }

    suspend fun validateAndCreateBooking(): Boolean {
        val state = _uiState.value
        var isValid = true

        if (state.date.isEmpty()) {
            _uiState.value = state.copy(dateError = "La fecha es obligatoria")
            isValid = false
        }

        if (state.entryTime.isEmpty()) {
            _uiState.value = _uiState.value.copy(entryTimeError = "La hora de entrada es obligatoria")
            isValid = false
        }

        if (state.exitTime.isEmpty()) {
            _uiState.value = _uiState.value.copy(exitTimeError = "La hora de salida es obligatoria")
            isValid = false
        }

        if (state.selectedVehicle == null) {
            _uiState.value = _uiState.value.copy(vehicleError = "Selecciona un vehículo")
            isValid = false
        }

        if (state.selectedParkingSpot == null) {
            _uiState.value = _uiState.value.copy(parkingSpotError = "Selecciona una plaza")
            isValid = false
        }

        if (isValid) {
            return createBooking()
        }

        return false
    }

    suspend fun createBooking(): Boolean {
        val state = _uiState.value

        if (state.selectedParkingSpot == null || state.selectedVehicle == null) {
            return false
        }

        _uiState.value = state.copy(isLoading = true)

        val newBooking = Booking(
            parkingSpot = state.selectedParkingSpot,
            entryTime = state.entryTime,
            exitTime = state.exitTime,
            date = state.date,
            vehicle = state.selectedVehicle,
            status = BookingStatus.CONFIRMADA,
            actualEntryTime = null,
            actualExitTime = null
        )

        return suspendCancellableCoroutine { continuation ->
            viewModelScope.launch {
                try {
                    val result = bookingRepository.createBooking(newBooking)
                    when {
                        result.isSuccess -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorCreatingBooking = false,
                                showSuccessDialog = true
                            )
                            continuation.resume(true)
                        }
                        result.isFailure -> {
                            _uiState.value = _uiState.value.copy(isLoading = false)
                            _uiState.value = _uiState.value.copy(errorCreatingBooking = true)
                            continuation.resume(false)
                        }
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorCreatingBooking = true)
                    continuation.resume(false)
                }
            }
        }
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(errorCreatingBooking = false)
    }

    fun dismissSuccess() {
        _uiState.value = _uiState.value.copy(showSuccessDialog = false)
    }

    fun resetForm() {
        _uiState.value = CreateBookingState()
    }
}

