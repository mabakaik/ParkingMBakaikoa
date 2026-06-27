package com.lksnext.parkingmbakaikoa.data.models

data class Vehicle (
    val plate: String,
    val type: VehicleType,
    val user: User
)
enum class VehicleType{
    CAR,
    MOTORBIKE,
    ELECTRIC_CAR
}