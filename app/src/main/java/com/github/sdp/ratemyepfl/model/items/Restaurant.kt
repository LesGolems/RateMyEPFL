package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    override val id: String,
    val lat: Double,
    val long: Double
) : Reviewable(){

    override fun toString(): String {
        return id
    }

    /**
     * Builder to create a restaurant step by step
     */
    class Builder : ReviewableBuilder<Restaurant> {
        private var id: String? = null
        private var lat: Double? = null
        private var long: Double? = null

        fun setId(id: String?) = apply {
            this.id = id
        }

        fun setLat(lat: Double?) = apply {
            this.lat = lat
        }

        fun setLong(long: Double?) = apply {
            this.long = long
        }

        override fun build(): Restaurant {
            val id = this asMandatory id
            val lat = this asMandatory lat
            val long = this asMandatory long
            return Restaurant(id, lat, long)
        }
    }

}