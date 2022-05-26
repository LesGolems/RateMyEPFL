package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.utils.MapActivityUtils
import kotlinx.serialization.Serializable

@Serializable
data class Restaurant constructor(
    val name: String = "",
    val occupancy: Int = 0,
    override val grade: Double = 0.0,
    override val numReviews: Int = 0,
    val lat: Double = 0.0,
    val long: Double = 0.0,
) : Reviewable(), DisplayableOnMap {

    companion object {
        const val MAX_OCCUPANCY = 50
    }

    override fun toString(): String {
        return name
    }

    override fun getId(): String = name

    override fun toMapItem(): MapItem {
        return RestaurantItem(
            this,
            MapActivityUtils.PHOTO_MAPPING.getOrDefault(
                getId(),
                R.raw.niki
            ), // Arbitrary default value
            R.drawable.ic_utensils_solid_small
        )
    }

}