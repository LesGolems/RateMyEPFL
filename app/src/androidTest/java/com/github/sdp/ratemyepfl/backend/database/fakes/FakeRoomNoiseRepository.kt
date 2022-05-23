package com.github.sdp.ratemyepfl.backend.database.fakes

import com.github.sdp.ratemyepfl.backend.database.RoomNoiseRepository
import com.github.sdp.ratemyepfl.model.NoiseInfo
import com.github.sdp.ratemyepfl.model.RoomNoiseInfo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class FakeRoomNoiseRepository @Inject constructor() : RoomNoiseRepository {

    companion object {
        val NO_MEASURE = RoomNoiseInfo("fakeId", listOf())
        val WITH_MEASURE = RoomNoiseInfo(
            "fakeId", listOf(
                NoiseInfo(LocalDateTime.now().toString(), 45),
                NoiseInfo(LocalDateTime.now().toString(), 67),
                NoiseInfo(LocalDateTime.now().toString(), 27),
                NoiseInfo(LocalDateTime.now().toString(), 95)
            )
        )
        var measureInfo = WITH_MEASURE
    }

    override suspend fun addMeasurement(
        roomId: String,
        date: LocalDateTime,
        measure: Int
    ): Task<Unit> {
        measureInfo = NO_MEASURE.copy(noiseData = listOf(NoiseInfo(date.toString(), measure)))
        return Mockito.mock(Task::class.java) as Task<Unit>
    }

    override suspend fun getRoomNoiseInfoById(roomId: String): RoomNoiseInfo = measureInfo
}