package com.lksnext.parkingmbakaikoa.ui.home.screens.bookingDetail

import androidx.lifecycle.ViewModel
import com.lksnext.parkingmbakaikoa.data.mock.MockData
import com.lksnext.parkingmbakaikoa.data.models.Booking
import com.lksnext.parkingmbakaikoa.data.models.BookingStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class BookingDetailViewModel : ViewModel() {
    
    // Lista mutable de bookings para simular cambios
    private val _booking = MutableStateFlow(MockData.mockBookings.get(0))
    
    // Booking seleccionado para mostrar en detalle
    val booking: StateFlow<Booking?> = _booking.asStateFlow()

    // Registrar hora de entrada real
    fun registerEntry(booking: Booking) {
        val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        val updatedBooking = booking.copy(
            actualEntryTime = currentTime,
            status = BookingStatus.EN_CURSO
        )
        updateBooking(updatedBooking)
    }
    
    // Registrar hora de salida real
    fun registerExit(booking: Booking) {
        val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        val updatedBooking = booking.copy(
            actualExitTime = currentTime,
            status = BookingStatus.TERMINADA
        )
        updateBooking(updatedBooking)
    }
    
    // Actualizar booking en la lista
    private fun updateBooking(newBooking: Booking) {
        _booking.value = newBooking
    }
}