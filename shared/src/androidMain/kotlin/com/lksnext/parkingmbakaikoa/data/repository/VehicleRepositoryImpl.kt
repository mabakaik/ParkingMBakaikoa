package com.lksnext.parkingmbakaikoa.data.repository

import com.lksnext.parkingmbakaikoa.data.mock.MockData
import com.lksnext.parkingmbakaikoa.data.models.Vehicle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

class VehicleRepositoryImpl : VehicleRepository {
    private val vehicles = MutableStateFlow(
        MockData.mockVehicles.toMutableList()
    )

    override suspend fun getUserVehicles(): Result<List<Vehicle>> {
        delay(300) // Simular latencia de red
        return try {
            // Filtrar vehículos del usuario actual (hardcoded "1" por ahora)
            val userVehicles = vehicles.value.filter { it.userId == "1" }
            Result.success(userVehicles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addVehicle(vehicle: Vehicle): Result<Unit> {
        delay(500) // Simular latencia de red
        return try {
            val newVehicle = vehicle.copy(id = "V${System.currentTimeMillis()}")
            vehicles.value = (vehicles.value + newVehicle).toMutableList()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteVehicle(vehicleId: String): Result<Unit> {
        delay(500) // Simular latencia de red
        return try {
            vehicles.value = vehicles.value.filter { it.id != vehicleId }.toMutableList()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

