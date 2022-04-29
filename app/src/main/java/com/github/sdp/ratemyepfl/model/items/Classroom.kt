package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepositoryImpl
import kotlinx.serialization.Serializable

@Serializable
data class Classroom(
    val name: String,
    override val numReviews: Int,
    override val averageGrade: Double,
    val roomKind: String? = null,
) : Reviewable() {

    override fun toString(): String = name

    override fun getId(): String = name

    /**
     * Creates an hash map of the Classroom
     */
    override fun toHashMap(): HashMap<String, Any?> = hashMapOf<String, Any?>(
        ClassroomRepositoryImpl.ROOM_NAME_FIELD_NAME to name,
        ClassroomRepositoryImpl.ROOM_KIND_FIELD_NAME to roomKind
    ).apply { this.putAll(super.toHashMap()) }


    /**
     * Builder to create a restaurant step by step.
     * Mandatory fields are:
     *  - [name]
     */
    class Builder(
        private var name: String? = null,
        private var numReviews: Int? = null,
        private var averageGrade: Double? = null,
        private var roomKind: String? = null,
    ) : ReviewableBuilder<Classroom> {

        fun setName(id: String?) = apply {
            this.name = id
        }

        fun setNumReviews(numReviews: Int?) = apply {
            this.numReviews = numReviews
        }

        fun setAverageGrade(averageGrade: Double?) = apply {
            this.averageGrade = averageGrade
        }

        fun setRoomKind(roomKind: String?) = apply {
            this.roomKind = roomKind
        }

        override fun build(): Classroom {
            val name = this asMandatory name
            val numReviews = this asMandatory numReviews
            val averageGrade = this asMandatory averageGrade
            return Classroom(name, numReviews, averageGrade, roomKind)
        }
    }

}
