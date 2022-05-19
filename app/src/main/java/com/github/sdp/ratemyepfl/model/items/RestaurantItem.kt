package com.github.sdp.ratemyepfl.model.items

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng

class RestaurantItem(restaurant: Restaurant, photo: Int, icon: BitmapDescriptor?) :
    MapItem(LatLng(restaurant.lat, restaurant.long), restaurant, restaurant.getId(), photo, icon) {
}