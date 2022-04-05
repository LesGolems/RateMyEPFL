package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.Serializable

@Serializable
data class Classroom(
    override val id: String,
    val roomKind: String? = null,
) : Reviewable() {

    override fun toString(): String {
        return id
    }

    /**
     * Builder to create a restaurant step by step
     */
    class Builder : ReviewableBuilder<Classroom> {
        private var id: String? = null
        private var roomKind: String? = null

        fun setId(id: String?) = apply {
            this.id = id
        }

        fun setRoomKind(roomKind: String?) = apply {
            this.roomKind = roomKind
        }

        override fun build(): Classroom {
            val id = this asMandatory id
            return Classroom(id, roomKind)
        }
    }

}
