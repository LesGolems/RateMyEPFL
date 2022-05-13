package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepositoryImpl
import kotlinx.serialization.Serializable

@Serializable
data class Classroom constructor(
    val name: String,
    override val grade: Double,
    override val numReviews: Int,
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
        private var grade: Double? = null,
        private var numReviews: Int? = null,
        private var roomKind: String? = null,
    ) : ReviewableBuilder<Classroom> {

        fun setName(id: String?) = apply {
            this.name = id
        }

        fun setRoomKind(roomKind: String?) = apply {
            this.roomKind = roomKind
        }

        fun setGrade(grade: Double?) = apply {
            this.grade = grade
        }

        fun setNumReviews(numReviews: Int?) = apply {
            this.numReviews = numReviews
        }

        override fun build(): Classroom {
            val name = this asMandatory name
            val grade = this asMandatory grade
            val numReviews = this asMandatory numReviews
            return Classroom(name, grade, numReviews, roomKind)
        }
    }

}
