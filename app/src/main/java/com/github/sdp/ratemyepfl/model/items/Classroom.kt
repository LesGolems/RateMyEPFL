package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepositoryImpl
import kotlinx.serialization.Serializable

@Serializable
data class Classroom(
    val name: String,
    val roomKind: String? = null,
) : Reviewable() {

    override fun toString(): String = name

    override fun getId(): String = name

    /**
     * Creates an hash map of the Classroom
     */
    override fun toHashMap(): HashMap<String, Any?> = hashMapOf(
        ClassroomRepositoryImpl.ROOM_NAME_FIELD_NAME to name,
        ClassroomRepositoryImpl.ROOM_KIND_FIELD_NAME to roomKind
    )


    /**
     * Builder to create a restaurant step by step.
     * Mandatory fields are:
     *  - [name]
     */
    class Builder(
        private var name: String? = null,
        private var roomKind: String? = null,
    ) : ReviewableBuilder<Classroom> {

        fun setName(id: String?) = apply {
            this.name = id
        }

        fun setRoomKind(roomKind: String?) = apply {
            this.roomKind = roomKind
        }

        override fun build(): Classroom {
            val name = this asMandatory name
            return Classroom(name, roomKind)
        }
    }

}
