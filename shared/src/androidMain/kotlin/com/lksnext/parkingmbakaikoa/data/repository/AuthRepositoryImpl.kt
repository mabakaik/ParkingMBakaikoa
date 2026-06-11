package com.lksnext.parkingmbakaikoa.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AuthRepositoryImpl : AuthRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun register(firstName: String, lastName: String, email: String, password: String): Result<Unit> =
        suspendCancellableCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    if (continuation.isActive) continuation.resume(Result.success(Unit))
                }
                .addOnFailureListener { exception: Exception ->
                    if (continuation.isActive) continuation.resume(Result.failure(exception))
                }
        }

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

    override suspend fun logout(): Result<Unit> = try {
        firebaseAuth.signOut()
        Result.success(Unit)
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> =
        suspendCancellableCoroutine { continuation ->
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    if (continuation.isActive) continuation.resume(Result.success(Unit))
                }
                .addOnFailureListener { exception: Exception ->
                    if (continuation.isActive) continuation.resume(Result.failure(exception))
                }
        }

    override fun observeAuthState(onAuthStateChanged: (Boolean) -> Unit): (() -> Unit) {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            onAuthStateChanged(firebaseAuth.currentUser != null)
        }
        firebaseAuth.addAuthStateListener(authStateListener)

        return { firebaseAuth.removeAuthStateListener(authStateListener) }
    }
}


