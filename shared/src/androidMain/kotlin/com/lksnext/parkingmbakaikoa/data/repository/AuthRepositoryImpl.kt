package com.lksnext.parkingmbakaikoa.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AuthRepositoryImpl : AuthRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun login(email: String, password: String): Result<Unit> =
        suspendCancellableCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    if (continuation.isActive) continuation.resume(Result.success(Unit))
                }
                .addOnFailureListener { exception: Exception ->
                    if (continuation.isActive) continuation.resume(Result.failure(exception))
                }
        }
}


