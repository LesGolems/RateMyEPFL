package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng

class EventItem(event: Event, photo: Int, icon: BitmapDescriptor?) :
    MapItem(LatLng(event.lat, event.long), event.getId(), photo, icon) {

    override val layout: Int = R.menu.bottom_navigation_menu_event_review
    override val graph: Int = R.navigation.nav_graph_event_review
}