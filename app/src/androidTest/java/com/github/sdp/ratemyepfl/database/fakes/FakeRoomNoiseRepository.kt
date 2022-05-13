package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.RoomNoiseRepository
import com.github.sdp.ratemyepfl.model.RoomNoiseInfo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject

class FakeRoomNoiseRepository @Inject constructor() : RoomNoiseRepository {

    companion object {
        val NO_MEASURE = RoomNoiseInfo("fakeId", mapOf())
        var measureInfo = NO_MEASURE
    }

    override suspend fun addMeasurement(
        roomId: String,
        date: LocalDateTime,
        measure: Int
    ): Task<Transaction> {
        measureInfo = NO_MEASURE.copy(noiseData = mapOf(Pair(date.toString(), measure)))
        return Mockito.mock(Task::class.java) as Task<Transaction>
    }

    override suspend fun getRoomNoiseInfoById(roomId: String): RoomNoiseInfo? = measureInfo
}