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
import kotlin.time.Clock
import kotlin.time.Instant
import org.jetbrains.compose.resources.getString
import parkingmbakaikoa.shared.generated.resources.Res
import parkingmbakaikoa.shared.generated.resources.dateOutOfRangeError
import parkingmbakaikoa.shared.generated.resources.dateRequired
import parkingmbakaikoa.shared.generated.resources.entryTimeRequired
import parkingmbakaikoa.shared.generated.resources.exitTimeBeforeEntryError
import parkingmbakaikoa.shared.generated.resources.exitTimeExceedsMaxDurationError
import parkingmbakaikoa.shared.generated.resources.exitTimeRequired
import parkingmbakaikoa.shared.generated.resources.incompatibleSpotTypeError
import parkingmbakaikoa.shared.generated.resources.selectSpotError
import parkingmbakaikoa.shared.generated.resources.selectVehicle
import kotlin.coroutines.resume

data class CreateBookingState(
    val date: String = "",
    val dateInMillis: Long = 0L,
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
    val showSuccessDialog: Boolean = false,
    val parkingSpotSearchEnabled: Boolean = false
)

class CreateBookingViewModel(
    private val bookingRepository: BookingRepository,
    initialParkingSpot: ParkingSpot? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateBookingState())
    val uiState: StateFlow<CreateBookingState> = _uiState.asStateFlow()

    private val userVehicles = MockData.mockVehicles

    private val availableParkingSpots = MockData.mockCalendarParkingSpots

    init{
        onDateChanged(Clock.System.now().toEpochMilliseconds())
        _uiState.value = _uiState.value.copy(
            entryTime = "08:00",
            exitTime = "17:00"
        )
        if (initialParkingSpot != null) {
            onParkingSpotSelected(initialParkingSpot)
        }
    }

    fun getUserVehicles(): List<Vehicle> = userVehicles

    fun getFilteredParkingSpots(): List<ParkingSpot> {
        val search = _uiState.value.parkingSpotSearch
        val selectedVehicle = _uiState.value.selectedVehicle

        return if (search.isEmpty() || selectedVehicle == null) {
            emptyList()
        } else {
            availableParkingSpots.filter {
                it.name.contains(search, ignoreCase = true) && it.type == selectedVehicle.type
            }
        }
    }

    private fun isDateInRange(dateInMillis: Long): Boolean {
        val now = Clock.System.now()
        // 7 días en milisegundos = 7 * 24 * 60 * 60 * 1000
        val sevenDaysInMillis = 7L * 24 * 60 * 60 * 1000
        val sevenDaysFromNow = Instant.fromEpochMilliseconds(now.toEpochMilliseconds() + sevenDaysInMillis)
        val selectedDate = Instant.fromEpochMilliseconds(dateInMillis)

        return selectedDate >= now && selectedDate <= sevenDaysFromNow
    }

    private fun parseTime(timeString: String): Pair<Int, Int>? {
        if (timeString.isEmpty()) return null
        val parts = timeString.split(":")
        if (parts.size != 2) return null
        return try {
            Pair(parts[0].toInt(), parts[1].toInt())
        } catch (_: Exception) {
            null
        }
    }

    private fun isExitTimeValid(entryTime: String, exitTime: String): Pair<Boolean, String?> {
        val entry = parseTime(entryTime) ?: return Pair(true, null)
        val exit = parseTime(exitTime) ?: return Pair(true, null)

        val entryMinutes = entry.first * 60 + entry.second
        val exitMinutes = exit.first * 60 + exit.second

        // Validar que la hora de salida no sea anterior a la de entrada
        if (exitMinutes <= entryMinutes) {
            return Pair(false, "exitTimeBeforeEntryError")
        }

        // Validar que la diferencia no sea mayor a 9 horas (540 minutos)
        val diffMinutes = exitMinutes - entryMinutes
        if (diffMinutes > 540) {
            return Pair(false, "exitTimeExceedsMaxDurationError")
        }

        return Pair(true, null)
    }

    fun onDateChanged(dateInMillis: Long) {
        val instant = Instant.fromEpochMilliseconds(dateInMillis)
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val monthNumber = (localDate.month.ordinal + 1)
        val formattedDate = "${localDate.day.toString().padStart(2, '0')}/" +
                "${monthNumber.toString().padStart(2, '0')}/" +
                "${localDate.year}"

        _uiState.value = _uiState.value.copy(
            date = formattedDate,
            dateInMillis = dateInMillis,
            dateError = null
        )
    }

    fun onEntryTimeChanged(time: String) {
        _uiState.value = _uiState.value.copy(entryTime = time, entryTimeError = null)
    }

    fun onExitTimeChanged(time: String) {
        _uiState.value = _uiState.value.copy(exitTime = time, exitTimeError = null)
    }

    fun onVehicleSelected(vehicle: Vehicle) {
        _uiState.value = _uiState.value.copy(
            selectedVehicle = vehicle,
            vehicleError = null,
            parkingSpotSearchEnabled = true,
            // Limpiar plaza seleccionada si el tipo cambió
            selectedParkingSpot = if (_uiState.value.selectedParkingSpot?.type != vehicle.type) null else _uiState.value.selectedParkingSpot,
            parkingSpotSearch = if (_uiState.value.selectedParkingSpot?.type != vehicle.type) "" else _uiState.value.parkingSpotSearch
        )
    }

    fun onParkingSpotSearchChanged(search: String) {
        // Solo permitir cambios si hay un vehículo seleccionado
        if (_uiState.value.selectedVehicle == null) {
            return
        }

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

        // Validar fecha no vacía
        if (state.date.isEmpty()) {
            _uiState.value = state.copy(dateError = getString(Res.string.dateRequired))
            isValid = false
        }
        // Validar rango de fecha (hoy a 7 días)
        else if (!isDateInRange(state.dateInMillis)) {
            _uiState.value = _uiState.value.copy(dateError = getString(Res.string.dateOutOfRangeError))
            isValid = false
        }

        // Validar hora de entrada
        if (state.entryTime.isEmpty()) {
            _uiState.value = _uiState.value.copy(entryTimeError = getString(Res.string.entryTimeRequired))
            isValid = false
        }

        // Validar hora de salida
        if (state.exitTime.isEmpty()) {
            _uiState.value = _uiState.value.copy(exitTimeError = getString(Res.string.exitTimeRequired))
            isValid = false
        }
        // Validar que hora de salida sea válida respecto a la de entrada
        else if (state.entryTime.isNotEmpty()) {
            val (isTimeValid, errorKey) = isExitTimeValid(state.entryTime, state.exitTime)
            if (!isTimeValid && errorKey != null) {
                val errorMessage = when (errorKey) {
                    "exitTimeBeforeEntryError" -> getString(Res.string.exitTimeBeforeEntryError)
                    "exitTimeExceedsMaxDurationError" -> getString(Res.string.exitTimeExceedsMaxDurationError)
                    else -> getString(Res.string.exitTimeRequired)
                }
                _uiState.value = _uiState.value.copy(exitTimeError = errorMessage)
                isValid = false
            }
        }

        // Validar vehículo seleccionado
        if (state.selectedVehicle == null) {
            _uiState.value = _uiState.value.copy(vehicleError = getString(Res.string.selectVehicle))
            isValid = false
        }

        // Validar plaza seleccionada
        if (state.selectedParkingSpot == null) {
            _uiState.value = _uiState.value.copy(parkingSpotError = getString(Res.string.selectSpotError))
            isValid = false
        }
        // Validar compatibilidad de tipo entre vehículo y plaza
        else if (state.selectedVehicle != null && state.selectedParkingSpot.type != state.selectedVehicle.type) {
            _uiState.value = _uiState.value.copy(parkingSpotError = getString(Res.string.incompatibleSpotTypeError))
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
                } catch (_: Exception) {
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

