package com.lksnext.parkingmbakaikoa.data.models

data class Booking(
    val id: String? = null,
    val parkingSpot: ParkingSpot,
    val entryTime: String,
    val exitTime: String,
    val date: String,
    val vehicle: Vehicle,
    val status: BookingStatus,
    val actualEntryTime: String? = null,  // Hora real de entrada
    val actualExitTime: String? = null    // Hora real de salida
)

enum class BookingStatus {
    EN_CURSO,
    CANCELADA,
    CONFIRMADA,
    TERMINADA
}
