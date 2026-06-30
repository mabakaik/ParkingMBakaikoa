package com.lksnext.parkingmbakaikoa.data.models

data class Vehicle (
    val id: String = "",
    val plate: String,
    val type: VehicleType,
    val userId: String
)
enum class VehicleType{
    CAR,
    MOTORBIKE,
    ELECTRIC_CAR
}