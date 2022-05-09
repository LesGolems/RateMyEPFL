package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.utils.MapActivityUtils
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    val name: String,
    val occupancy: Int,
    val lat: Double,
    val long: Double,
) : Reviewable(), Displayable {

    companion object {
        const val MAX_OCCUPANCY = 50
    }

    override fun toString(): String {
        return name
    }

    override fun getId(): String = name

    /**
     * Creates an hash map of the Restaurant, to add it to the DB
     */
    override fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            RestaurantRepositoryImpl.RESTAURANT_NAME_FIELD_NAME to name,
            RestaurantRepositoryImpl.OCCUPANCY_FIELD_NAME to occupancy,
            RestaurantRepositoryImpl.LATITUDE_FIELD_NAME to lat,
            RestaurantRepositoryImpl.LONGITUDE_FIELD_NAME to long,
        )
    }

    override fun toMapItem(): MapItem {
        return RestaurantItem(
            this,
            MapActivityUtils.PHOTO_MAPPING.getOrDefault(
                getId(),
                R.raw.niki
            ), // Arbitrary default value
            BitmapDescriptorFactory.fromResource(R.raw.restaurant_marker)
        )
    }

    /**
     * Builder to create a restaurant step by step
     * Mandatory fields are:
     *  - [name]
     */
    class Builder(
        private var name: String? = null,
        private var occupancy: Int? = 0,
        private var lat: Double? = null,
        private var long: Double? = null
    ) : ReviewableBuilder<Restaurant> {


        fun setName(name: String?) = apply {
            this.name = name
        }

        fun setLat(lat: Double?) = apply {
            this.lat = lat
        }

        fun setLong(long: Double?) = apply {
            this.long = long
        }

        fun setOccupancy(occupancy: Int?) = apply {
            this.occupancy = occupancy
        }

        override fun build(): Restaurant {
            val name = this asMandatory name
            val occupancy = this asMandatory occupancy
            val lat = this asMandatory lat
            val long = this asMandatory long
            return Restaurant(name, occupancy, lat, long)
        }
    }

}