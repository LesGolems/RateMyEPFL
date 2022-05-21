package com.github.sdp.ratemyepfl.backend.database.firebase

import com.github.sdp.ratemyepfl.backend.database.Repository
import com.github.sdp.ratemyepfl.backend.database.RoomNoiseRepository
import com.github.sdp.ratemyepfl.model.NoiseInfo
import com.github.sdp.ratemyepfl.model.RoomNoiseInfo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

        fun DocumentSnapshot.toRoomNoiseInfo(): RoomNoiseInfo? {
            return try {
                val type = object : TypeToken<List<NoiseInfo>>() {}.type
                val noiseData = getString(ROOMS_INFO_FIELD_NAME)?.let {
                    Gson().fromJson<List<NoiseInfo>>(it, type)
                }
                RoomNoiseInfo.Builder(
                    getString(ROOM_NAME_FIELD_NAME),
                    noiseData
                ).build()
            } catch (e: IllegalStateException) {
                null
            }
        }
    }

    override suspend fun addMeasurement(
        roomId: String,
        date: LocalDateTime,
        measure: Int
    ): Task<Transaction> {

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
        }
    }

    override suspend fun getRoomNoiseInfoById(roomId: String): RoomNoiseInfo? =
        repository.getById(roomId)
            .toRoomNoiseInfo()

}