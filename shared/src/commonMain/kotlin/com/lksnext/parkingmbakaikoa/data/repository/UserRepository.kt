package com.lksnext.parkingmbakaikoa.data.repository

import com.lksnext.parkingmbakaikoa.data.models.User

interface UserRepository {
    suspend fun getCurrentUser(): Result<User>
    suspend fun updateUser(user: User): Result<Unit>
}

