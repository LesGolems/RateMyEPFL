package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.RepositoryImpl.Companion.toItem
import com.github.sdp.ratemyepfl.model.NoiseInfo
import com.github.sdp.ratemyepfl.model.RoomNoiseInfo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

class RoomNoiseRepositoryImpl(val repository: RepositoryImpl<RoomNoiseInfo>) : RoomNoiseRepository,
    Repository<RoomNoiseInfo> by repository {

    @Inject
    constructor(db: FirebaseFirestore) : this(RepositoryImpl<RoomNoiseInfo>(
        db,
        ROOM_INFO_COLLECTION_PATH
    ) {
        it.toRoomNoiseInfo()
    })

    companion object {
        const val ROOM_INFO_COLLECTION_PATH = "rooms_noise_info"
        const val ROOM_NAME_FIELD_NAME = "name"
        const val ROOMS_INFO_FIELD_NAME = "noiseData"

        fun DocumentSnapshot.toRoomNoiseInfo(): RoomNoiseInfo? = toItem()
    }

    override suspend fun addMeasurement(
        roomId: String,
        date: LocalDateTime,
        measure: Int
    ): Task<Unit> {

        if (getRoomNoiseInfoById(roomId) == null) {
            repository.add(RoomNoiseInfo(roomId)).await()
        }

        return repository.update(roomId) {
            val newData = it.noiseData.plus(
                NoiseInfo(
                    date.toString(),
                    measure
                )
            )
            it.copy(
                noiseData = newData
            )
        }.continueWith {}
    }

    override suspend fun getRoomNoiseInfoById(roomId: String): RoomNoiseInfo? =
        repository.getById(roomId)
            .toRoomNoiseInfo()

}