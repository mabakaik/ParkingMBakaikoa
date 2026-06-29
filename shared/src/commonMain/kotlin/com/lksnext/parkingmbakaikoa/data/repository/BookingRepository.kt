package com.lksnext.parkingmbakaikoa.data.repository

import com.lksnext.parkingmbakaikoa.data.models.Booking
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    suspend fun createBooking(booking: Booking):  Result<String>
}

