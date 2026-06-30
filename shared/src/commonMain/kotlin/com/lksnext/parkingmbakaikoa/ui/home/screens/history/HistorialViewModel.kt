package com.lksnext.parkingmbakaikoa.ui.home.screens.history

import androidx.lifecycle.ViewModel
import com.lksnext.parkingmbakaikoa.data.mock.MockData
import com.lksnext.parkingmbakaikoa.data.models.Booking
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class HistorialViewModel : ViewModel() {

    companion object {
        private const val HISTORY_DAYS = 30

        // Formateador para Booking.date, p.ej. "11 Jun 2026"
        private val DATE_FORMAT = LocalDate.Format {
            day()
            char(' ')
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            year()
        }
    }

    // Lista completa de reservas (mock)
    private val _bookings = MutableStateFlow(MockData.mockBookings)

    // Reserva seleccionada para mostrar en detalle
    private val _selectedBooking = MutableStateFlow<Booking?>(null)
    val selectedBooking: StateFlow<Booking?> = _selectedBooking.asStateFlow()

    /**
     * Devuelve las reservas de los últimos 30 días, ordenadas de la más
     * reciente a la más antigua.
     */
    fun getHistoryBookings(): List<Booking> {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val limitDate = today.minus(HISTORY_DAYS, DateTimeUnit.DAY)

        return _bookings.value
            .mapNotNull { booking ->
                val date = parseBookingDate(booking.date)
                if (date != null) booking to date else null
            }
            .filter { (_, date) -> date >= limitDate && date <= today }
            .sortedByDescending { (_, date) -> date }
            .map { (booking, _) -> booking }
    }

    private fun parseBookingDate(rawDate: String): LocalDate? {
        return try {
            DATE_FORMAT.parse(rawDate.trim())
        } catch (e: Exception) {
            null
        }
    }

    // Seleccionar una reserva para ver detalles
    fun selectBooking(booking: Booking) {
        _selectedBooking.value = booking
    }

    // Limpiar selección (volver a la lista)
    fun clearSelection() {
        _selectedBooking.value = null
    }
}


