package com.github.sdp.ratemyepfl.model.items

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.github.sdp.ratemyepfl.activity.MainActivity
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng

class EventItem(event: Event, photo: Int, icon: BitmapDescriptor?) :
    MapItem(LatLng(event.lat, event.long), event.id, photo, icon) {

    override fun onClickIntent(activity: FragmentActivity?): Intent {
        return Intent(activity, MainActivity::class.java) // WILL CHANGE WHEN EVENT ACTIVITY WILL BE CREATED
    }
}