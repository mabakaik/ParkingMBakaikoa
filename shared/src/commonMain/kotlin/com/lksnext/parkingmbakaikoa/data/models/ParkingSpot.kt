package com.lksnext.parkingmbakaikoa.data.models

data class ParkingSpot(
    val id: String? = null,
    val name: String,
    val status: String,
    val type: VehicleType = VehicleType.CAR,
)

