package com.github.sdp.ratemyepfl.model.items

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng

class RestaurantItem(restaurant: Restaurant, photo: Int, icon: BitmapDescriptor?) :
      MapItem(LatLng(restaurant.lat, restaurant.long), restaurant.getId(), photo, icon) {

    override fun onClickIntent(activity: FragmentActivity?): Intent {
        val intent = Intent(activity, ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, name)
        intent.putExtra(ReviewActivity.EXTRA_LAYOUT_ID, R.layout.activity_restaurant_review) // Will have to change later
        return intent
    }
}