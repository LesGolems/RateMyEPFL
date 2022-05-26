package com.github.sdp.ratemyepfl.model.items

import com.google.android.gms.maps.model.LatLng

class EventItem(event: Event, photo: Int, icon: Int) :
    MapItem(LatLng(event.lat, event.long), event, event.getId(), event.name, photo, icon)