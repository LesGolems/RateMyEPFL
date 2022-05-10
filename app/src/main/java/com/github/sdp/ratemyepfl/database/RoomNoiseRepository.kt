package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.RoomNoiseInfo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import java.time.LocalDateTime

interface RoomNoiseRepository {

    suspend fun addMeasurement(roomId: String, date: LocalDateTime, measure: Int): Task<Transaction>

    suspend fun getRoomNoiseInfoById(roomId: String): RoomNoiseInfo?
}