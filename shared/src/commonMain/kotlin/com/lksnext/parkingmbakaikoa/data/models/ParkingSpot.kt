package com.lksnext.parkingmbakaikoa.data.models

data class ParkingSpot(
    val spaceName: String,
    val entryTime: String,
    val exitTime: String,
    val date: String,
    val status: String,
    val vehicle: Vehicle,
    val user: User
)

