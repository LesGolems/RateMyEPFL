package com.github.sdp.ratemyepfl.model.items

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem


class RestaurantItem(private val position: LatLng,
                     val name: String,
                     val photo: Int) : ClusterItem {

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSnippet(): String? {
        return null
    }
}