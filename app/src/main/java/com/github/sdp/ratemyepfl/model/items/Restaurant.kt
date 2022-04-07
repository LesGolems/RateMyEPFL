package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    override val id: String,
    val occupancy: Int,
    val lat: Double,
    val long: Double,
    override val numReviews: Int,
    override val averageGrade: Double,
) : Reviewable() {

    override fun toString(): String {
        return id
    }

    /**
     * Builder to create a restaurant step by step
     * Mandatory fields are:
     *  - [id]
     */
    class Builder : ReviewableBuilder<Restaurant> {
        private var id: String? = null
        private var numReviews: Int? = null
        private var averageGrade: Double? = null
        private var lat: Double? = null
        private var long: Double? = null

        fun setId(id: String?) = apply {
            this.id = id
        }

        fun setNumReviews(numReviews: Int?) = apply {
            this.numReviews = numReviews
        }

        fun setAverageGrade(averageGrade: Double?) = apply {
            this.averageGrade = averageGrade
        }

        fun setLat(lat: Double?) = apply {
            this.lat = lat
        }

        fun setLong(long: Double?) = apply {
            this.long = long
        }

        override fun build(): Restaurant {
            val id = this asMandatory id
            val numReviews = this asMandatory numReviews
            val averageGrade = this asMandatory averageGrade
            val lat = this asMandatory lat
            val long = this asMandatory long
            return Restaurant(id, 0, lat, long, numReviews, averageGrade)
        }
    }

}