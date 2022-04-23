package com.github.sdp.ratemyepfl.model.items

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng

class EventItem(event: Event, photo: Int, icon: BitmapDescriptor?) :
    MapItem(LatLng(event.lat, event.long), event.id, photo, icon) {

    override fun onClickIntent(activity: FragmentActivity?): Intent {
        TODO()
    }
}