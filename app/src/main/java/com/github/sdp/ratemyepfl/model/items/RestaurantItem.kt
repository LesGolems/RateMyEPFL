package com.github.sdp.ratemyepfl.model.items

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem


class RestaurantItem(private val mPosition: LatLng,
                     val name: String,
                     val photo: Int) : ClusterItem {

    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSnippet(): String? {
        return null
    }
}