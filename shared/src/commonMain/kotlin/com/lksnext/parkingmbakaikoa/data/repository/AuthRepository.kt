package com.lksnext.parkingmbakaikoa.data.repository

interface AuthRepository {
    suspend fun register(firstName: String, lastName: String, email: String, password: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun isUserLoggedIn(): Boolean
    fun observeAuthState(onAuthStateChanged: (Boolean) -> Unit): (() -> Unit)
}

