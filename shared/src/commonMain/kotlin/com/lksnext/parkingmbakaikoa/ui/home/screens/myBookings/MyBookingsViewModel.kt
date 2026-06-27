package com.lksnext.parkingmbakaikoa.ui.home.screens.myBookings

import androidx.lifecycle.ViewModel
import com.lksnext.parkingmbakaikoa.data.mock.MockData
import com.lksnext.parkingmbakaikoa.data.models.Booking
import com.lksnext.parkingmbakaikoa.data.models.BookingStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MyBookingsViewModel : ViewModel() {
    
    // Lista mutable de bookings para simular cambios
    private val _bookings = MutableStateFlow(MockData.mockBookings.toMutableList())
    
    // Booking seleccionado para mostrar en detalle
    private val _selectedBooking = MutableStateFlow<Booking?>(null)
    val selectedBooking: StateFlow<Booking?> = _selectedBooking.asStateFlow()
    
    // Obtener todas las reservas
    fun getAllBookings(): List<Booking> {
        return _bookings.value
    }
    
    // Obtener solo reservas confirmadas y en curso
    fun getActiveBookings(): List<Booking> {
        return _bookings.value.filter { booking ->
            booking.status == BookingStatus.CONFIRMADA || 
            booking.status == BookingStatus.EN_CURSO
        }
    }
    
    // Obtener el número total de reservas
    fun getTotalBookingsCount(): Int {
        return _bookings.value.size
    }
    
    // Obtener el número de reservas activas
    fun getActiveBookingsCount(): Int {
        return getActiveBookings().size
    }
    
    // Seleccionar una reserva para ver detalles
    fun selectBooking(booking: Booking) {
        _selectedBooking.value = booking
    }
    
    // Limpiar selección (volver a lista)
    fun clearSelection() {
        _selectedBooking.value = null
    }
    
    // Registrar hora de entrada real
    fun registerEntry(booking: Booking) {
        val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        val updatedBooking = booking.copy(
            actualEntryTime = currentTime,
            status = BookingStatus.EN_CURSO
        )
        updateBooking(booking, updatedBooking)
    }
    
    // Registrar hora de salida real
    fun registerExit(booking: Booking) {
        val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        val updatedBooking = booking.copy(
            actualExitTime = currentTime,
            status = BookingStatus.TERMINADA
        )
        updateBooking(booking, updatedBooking)
    }
    
    // Actualizar booking en la lista
    private fun updateBooking(oldBooking: Booking, newBooking: Booking) {
        val currentList = _bookings.value.toMutableList()
        val index = currentList.indexOf(oldBooking)
        if (index != -1) {
            currentList[index] = newBooking
            _bookings.value = currentList
            _selectedBooking.value = newBooking
        }
    }
}