package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.Serializable

@Serializable
data class Classroom(
    override val id: String,
    override val numReviews: Int,
    override val averageGrade: Double,
    val roomKind: String? = null,
) : Reviewable() {

    override fun toString(): String {
        return id
    }

    /**
     * Builder to create a restaurant step by step.
     * Mandatory fields are:
     *  - [id]
     */
    class Builder : ReviewableBuilder<Classroom> {
        private var id: String? = null
        private var numReviews: Int? = null
        private var averageGrade: Double? = null
        private var roomKind: String? = null

        fun setId(id: String?) = apply {
            this.id = id
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
            val id = this asMandatory id
            val numReviews = this asMandatory numReviews
            val averageGrade = this asMandatory averageGrade
            return Classroom(id, numReviews, averageGrade, roomKind)
        }
    }

}
