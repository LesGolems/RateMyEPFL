package com.github.sdp.ratemyepfl.model.items

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    override val id: String,
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