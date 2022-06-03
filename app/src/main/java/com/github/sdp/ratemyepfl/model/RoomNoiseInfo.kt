package com.github.sdp.ratemyepfl.model

import com.github.sdp.ratemyepfl.backend.database.RepositoryItem
import kotlinx.serialization.Serializable

@Serializable
data class RoomNoiseInfo(
    val roomId: String = "",
    val noiseData: List<NoiseInfo> = listOf()
) : RepositoryItem {

    override fun getId(): String = roomId

    class Builder(
        private val roomId: String?,
        private val noiseData: List<NoiseInfo>? = listOf(),
    ) {
        fun build(): RoomNoiseInfo {
            val roomId = this.roomId
            val noiseData = this.noiseData ?: listOf()

            if (roomId != null) {
                return RoomNoiseInfo(roomId, noiseData)
            } else {
                throw IllegalStateException("Cannot build a room noise info with null arguments")
            }
        }
    }
}