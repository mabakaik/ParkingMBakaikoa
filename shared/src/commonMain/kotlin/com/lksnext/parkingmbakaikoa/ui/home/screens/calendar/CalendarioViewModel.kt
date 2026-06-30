package com.lksnext.parkingmbakaikoa.ui.home.screens.calendar

import androidx.lifecycle.ViewModel
import com.lksnext.parkingmbakaikoa.data.mock.MockData
import com.lksnext.parkingmbakaikoa.data.models.ParkingSpot
import com.lksnext.parkingmbakaikoa.data.models.VehicleType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

const val CALENDAR_PAGE_SIZE = 8
private const val CALENDAR_DAYS = 8

data class CalendarDay(
    val date: LocalDate,
    val dayOfWeek: DayOfWeek,
    val dayNumber: Int,
    val isToday: Boolean
)

data class CalendarioState(
    val days: List<CalendarDay> = emptyList(),
    val selectedDate: LocalDate? = null,
    val selectedType: VehicleType? = null,
    val currentPage: Int = 0,
    val spots: List<ParkingSpot> = emptyList(),
    val pageCount: Int = 1
)

class CalendarioViewModel : ViewModel() {

    private val allSpots = MockData.mockCalendarParkingSpots

    private val _uiState = MutableStateFlow(CalendarioState())
    val uiState: StateFlow<CalendarioState> = _uiState.asStateFlow()

    // Plaza seleccionada para compartir con la pantalla de creación de reserva
    private val _selectedSpot = MutableStateFlow<ParkingSpot?>(null)
    val selectedSpot: StateFlow<ParkingSpot?> = _selectedSpot.asStateFlow()

    init {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val days = (0 until CALENDAR_DAYS).map { offset ->
            val date = today.plus(offset, DateTimeUnit.DAY)
            CalendarDay(
                date = date,
                dayOfWeek = date.dayOfWeek,
                dayNumber = date.day,
                isToday = offset == 0
            )
        }
        _uiState.value = _uiState.value.copy(days = days, selectedDate = today)
        recomputeSpots()
    }

    fun onDaySelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date, currentPage = 0)
        recomputeSpots()
    }

    fun onTypeFilterSelected(type: VehicleType?) {
        _uiState.value = _uiState.value.copy(selectedType = type, currentPage = 0)
        recomputeSpots()
    }

    fun onNextPage() {
        val state = _uiState.value
        if (state.currentPage < state.pageCount - 1) {
            _uiState.value = state.copy(currentPage = state.currentPage + 1)
        }
    }

    fun onPreviousPage() {
        val state = _uiState.value
        if (state.currentPage > 0) {
            _uiState.value = state.copy(currentPage = state.currentPage - 1)
        }
    }

    fun selectSpot(spot: ParkingSpot) {
        _selectedSpot.value = spot
    }

    fun clearSelectedSpot() {
        _selectedSpot.value = null
    }

    /**
     * Devuelve solo las plazas de la página actual ya filtradas por tipo, con su
     * estado (Libre/Ocupada) calculado de forma determinista para el día seleccionado.
     */
    fun getCurrentPageSpots(): List<ParkingSpot> {
        val state = _uiState.value
        val from = state.currentPage * CALENDAR_PAGE_SIZE
        val to = minOf(from + CALENDAR_PAGE_SIZE, state.spots.size)
        if (from >= state.spots.size) return emptyList()
        return state.spots.subList(from, to)
    }

    private fun recomputeSpots() {
        val state = _uiState.value
        val date = state.selectedDate ?: return

        val filtered = allSpots
            .filter { state.selectedType == null || it.type == state.selectedType }
            .map { spot ->
                val isFree = isSpotFree(date, spot)
                spot.copy(status = if (isFree) "Libre" else "Ocupada")
            }

        val pageCount = if (filtered.isEmpty()) 1 else (filtered.size + CALENDAR_PAGE_SIZE - 1) / CALENDAR_PAGE_SIZE
        val safePage = state.currentPage.coerceIn(0, pageCount - 1)

        _uiState.value = state.copy(
            spots = filtered,
            pageCount = pageCount,
            currentPage = safePage
        )
    }

    /**
     * Estado variable y determinista por combinación de día + plaza, de forma que
     * un mismo día muestre siempre el mismo resultado.
     */
    private fun isSpotFree(date: LocalDate, spot: ParkingSpot): Boolean {
        val seed = date.toEpochDays().toInt() + spot.name.hashCode()
        return (seed % 3) != 0
    }
}



