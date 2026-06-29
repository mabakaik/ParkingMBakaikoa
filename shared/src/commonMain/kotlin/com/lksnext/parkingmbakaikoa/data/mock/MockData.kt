package com.lksnext.parkingmbakaikoa.data.mock

import com.lksnext.parkingmbakaikoa.data.models.Booking
import com.lksnext.parkingmbakaikoa.data.models.BookingStatus
import com.lksnext.parkingmbakaikoa.data.models.ParkingSpot
import com.lksnext.parkingmbakaikoa.data.models.User
import com.lksnext.parkingmbakaikoa.data.models.Vehicle
import com.lksnext.parkingmbakaikoa.data.models.VehicleType

object MockData {
    // Mock Users
    val mockUser1 = User(
        name = "Maria",
        email = "maria@example.com"
    )

    val mockUser2 = User(
        name = "Juan",
        email = "juan@example.com"
    )

    val mockUser3 = User(
        name = "Ana",
        email = "ana@example.com"
    )

    // Mock Vehicles
    val mockVehicleCar1 = Vehicle(
        plate = "1234 ABC",
        type = VehicleType.CAR,
        user = mockUser1
    )

    val mockVehicleCar2 = Vehicle(
        plate = "5678 DEF",
        type = VehicleType.CAR,
        user = mockUser2
    )

    val mockVehicleMotorbike = Vehicle(
        plate = "9012 GHI",
        type = VehicleType.MOTORBIKE,
        user = mockUser3
    )

    val mockVehicleElectric = Vehicle(
        plate = "3456 JKL",
        type = VehicleType.ELECTRIC_CAR,
        user = mockUser1
    )

    // Mock Parking Spots
    val mockParkingSpot1 = ParkingSpot(
        id = "A15",
        name = "A15",
        status = "Ocupada"
    )

    val mockParkingSpot2 = ParkingSpot(
        id = "B22",
        name = "B22",
        status = "Reservada"
    )

    val mockParkingSpot3 = ParkingSpot(
        id = "C05",
        name = "C05",
        status = "Libre"
    )

    // Mock Bookings
    val mockBooking1 = Booking(
        id = "BK001",
        parkingSpot = mockParkingSpot1,
        entryTime = "09:00",
        exitTime = "17:30",
        date = "11 Jun 2026",
        status = BookingStatus.CONFIRMADA,
        vehicle = mockVehicleCar1,
        actualEntryTime = null,
        actualExitTime = null
    )

    val mockBooking2 = Booking(
        id = "BK002",
        parkingSpot = mockParkingSpot2,
        entryTime = "08:30",
        exitTime = "18:00",
        date = "26 Jun 2026",
        status = BookingStatus.EN_CURSO,
        vehicle = mockVehicleCar2,
        actualEntryTime = "08:35",  // Ya registró entrada
        actualExitTime = null
    )

    val mockBooking3 = Booking(
        id = "BK003",
        parkingSpot = mockParkingSpot3,
        entryTime = "10:00",
        exitTime = "14:00",
        date = "27 Jun 2026",
        status = BookingStatus.CONFIRMADA,
        vehicle = mockVehicleMotorbike,
        actualEntryTime = null,
        actualExitTime = null
    )

    val mockBooking4 = Booking(
        id = "BK004",
        parkingSpot = mockParkingSpot1.copy(
            id = "D12",
            name = "D12",
            status = "Cancelada"
        ),
        entryTime = "07:00",
        exitTime = "15:00",
        date = "20 Jun 2026",
        status = BookingStatus.CANCELADA,
        vehicle = mockVehicleElectric,
        actualEntryTime = null,
        actualExitTime = null
    )

    val mockBooking5 = Booking(
        id = "BK005",
        parkingSpot = mockParkingSpot2.copy(
            id = "E08",
            name = "E08",
            status = "Terminada"
        ),
        entryTime = "09:30",
        exitTime = "17:00",
        date = "15 Jun 2026",
        status = BookingStatus.TERMINADA,
        vehicle = mockVehicleCar1,
        actualEntryTime = "09:28",  // Registró entrada
        actualExitTime = "17:05"    // Registró salida
    )

    // Listas de datos mock para usar en tests o previews
    val mockUsers = listOf(mockUser1, mockUser2, mockUser3)
    val mockVehicles = listOf(mockVehicleCar1, mockVehicleCar2, mockVehicleMotorbike, mockVehicleElectric)
    val mockParkingSpots = listOf(mockParkingSpot1, mockParkingSpot2, mockParkingSpot3)
    val mockBookings = listOf(mockBooking1, mockBooking2, mockBooking3, mockBooking4, mockBooking5)
}
