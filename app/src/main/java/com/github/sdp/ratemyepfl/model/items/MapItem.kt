package com.github.sdp.ratemyepfl.model.items

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem


abstract class MapItem(private val position: LatLng,
                       val name: String,
                       val photo: Int,
                       val icon: BitmapDescriptor?) : ClusterItem {

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSnippet(): String? {
        return null
    }

    abstract fun onClickIntent(activity: FragmentActivity?): Intent
}