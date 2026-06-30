package com.lksnext.parkingmbakaikoa.data.repository

import com.lksnext.parkingmbakaikoa.data.mock.MockData
import com.lksnext.parkingmbakaikoa.data.models.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

class UserRepositoryImpl : UserRepository {
    private val currentUser = MutableStateFlow(MockData.mockUser1)

    override suspend fun getCurrentUser(): Result<User> {
        delay(300) // Simular latencia de red
        return try {
            Result.success(currentUser.value)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        delay(500) // Simular latencia de red
        return try {
            currentUser.value = user
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

