package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    override val id: String,
    val occupancy: Int = 0,
    val latitude: Long = 0,
    val longitude: Long = 0
) : Reviewable() {

    override fun toString(): String {
        return id
    }

    /**
     * Builder to create a restaurant step by step
     */
    class Builder : ReviewableBuilder<Restaurant> {
        private var id: String? = null

        fun setId(id: String?) = apply {
            this.id = id
        }

        override fun build(): Restaurant {
            val id = this asMandatory id
            return Restaurant(id)
        }
    }

}