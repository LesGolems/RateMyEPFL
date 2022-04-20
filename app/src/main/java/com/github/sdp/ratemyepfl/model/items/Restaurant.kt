package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl
import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    val name: String,
    val occupancy: Int,
    val lat: Double,
    val long: Double,
    override val numReviews: Int,
    override val averageGrade: Double,
) : Reviewable() {

    override fun toString(): String {
        return name
    }

    override fun getId(): String = name
    /**
     * Creates an hash map of the Course, to add it to the DB
     */
    override fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            RestaurantRepositoryImpl.OCCUPANCY_FIELD_NAME to occupancy.toString(),
            RestaurantRepositoryImpl.LATITUDE_FIELD_NAME to lat.toString(),
            RestaurantRepositoryImpl.LONGITUDE_FIELD_NAME to long.toString(),
            ReviewableRepositoryImpl.NUM_REVIEWS_FIELD_NAME to numReviews.toString(),
            ReviewableRepositoryImpl.AVERAGE_GRADE_FIELD_NAME to averageGrade.toString()
        )
    }

    /**
     * Builder to create a restaurant step by step
     * Mandatory fields are:
     *  - [id]
     */
    class Builder : ReviewableBuilder<Restaurant> {
        private var name: String? = null
        private var numReviews: Int? = null
        private var averageGrade: Double? = null
        private var lat: Double? = null
        private var long: Double? = null

        fun setName(name: String?) = apply {
            this.name = name
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
            val name = this asMandatory name
            val numReviews = this asMandatory numReviews
            val averageGrade = this asMandatory averageGrade
            val lat = this asMandatory lat
            val long = this asMandatory long
            return Restaurant(name, 0, lat, long, numReviews, averageGrade)
        }
    }

}