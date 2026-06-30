package com.lksnext.parkingmbakaikoa.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lksnext.parkingmbakaikoa.data.models.User
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume

class AuthRepositoryImpl : AuthRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun register(firstName: String, lastName: String, email: String, password: String): Result<Unit> {
        return try {
            //Register in Firebase Auth
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            val userId = authResult.user?.uid
                ?: return Result.failure(Exception("No se pudo obtener el ID del usuario"))

            //Register in firestore
            val user = User(
                email = email,
                firstName = firstName,
                lastName = lastName,
            )

            db.collection("users")
                .document(userId)
                .set(user)
                .await()

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
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


