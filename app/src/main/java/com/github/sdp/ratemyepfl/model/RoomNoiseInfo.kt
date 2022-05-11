package com.github.sdp.ratemyepfl.model

import android.graphics.Color
import com.github.sdp.ratemyepfl.database.RepositoryItem
import com.github.sdp.ratemyepfl.database.RoomNoiseRepositoryImpl
import com.google.gson.Gson
import kotlinx.serialization.Serializable

@Serializable
data class RoomNoiseInfo(
    val roomId: String,
    val noiseData: Map<String, Int> = mapOf()
) : RepositoryItem {

    override fun getId(): String = roomId

    override fun toHashMap(): HashMap<String, Any?> {
        val json = Gson().toJson(noiseData)
        return hashMapOf(
            RoomNoiseRepositoryImpl.ROOM_NAME_FIELD_NAME to roomId,
            RoomNoiseRepositoryImpl.ROOMS_INFO_FIELD_NAME to json
        )
    }

    class Builder(
        private val roomId: String?,
        private val noiseData: Map<String, Int>? = mapOf(),
    ) {
        fun build(): RoomNoiseInfo {
            val roomId = this.roomId
            val noiseData = this.noiseData ?: mapOf()

            if (roomId != null) {
                return RoomNoiseInfo(roomId, noiseData)
            } else {
                throw IllegalStateException("Cannot build a room noise info with null arguments")
            }
        }
    }
}