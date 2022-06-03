package com.github.sdp.ratemyepfl.model.items

import com.google.android.gms.maps.model.LatLng

class RestaurantItem(restaurant: Restaurant, photo: Int, icon: Int) :
    MapItem(
        LatLng(restaurant.lat, restaurant.long),
        restaurant, restaurant.getId(), restaurant.name, photo, icon
    )