package com.lksnext.parkingmbakaikoa.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.lksnext.parkingmbakaikoa.data.models.Booking
import kotlinx.coroutines.tasks.await

class BookingRepositoryImpl : BookingRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun createBooking(booking: Booking): Result<String> {
        return try {
            val ref = db.collection("bookings")
                .add(booking)
                .await()

            Result.success(ref.id)

        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}


