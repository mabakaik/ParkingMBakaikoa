package com.lksnext.parkingmbakaikoa.data.repository

import com.lksnext.parkingmbakaikoa.data.models.Vehicle

interface VehicleRepository {
    suspend fun getUserVehicles(): Result<List<Vehicle>>
    suspend fun addVehicle(vehicle: Vehicle): Result<Unit>
    suspend fun deleteVehicle(vehicleId: String): Result<Unit>
}

