package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng

class RestaurantItem(restaurant: Restaurant, photo: Int, icon: BitmapDescriptor?) :
    MapItem(LatLng(restaurant.lat, restaurant.long), restaurant.getId(), photo, icon) {

    override val layout: Int = R.menu.bottom_navigation_menu_restaurant_review
    override val graph: Int = R.navigation.nav_graph_restaurant_review
}