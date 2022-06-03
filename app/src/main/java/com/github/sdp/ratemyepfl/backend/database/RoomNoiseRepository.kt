package com.github.sdp.ratemyepfl.backend.database

import com.github.sdp.ratemyepfl.model.RoomNoiseInfo
import java.time.LocalDateTime

interface RoomNoiseRepository {

    suspend fun addMeasurement(roomId: String, date: LocalDateTime, measure: Int)

    suspend fun getRoomNoiseInfoById(roomId: String): RoomNoiseInfo?
}