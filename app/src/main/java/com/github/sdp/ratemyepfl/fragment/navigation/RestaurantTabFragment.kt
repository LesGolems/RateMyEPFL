package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.restaurants.RestaurantListActivity

class RestaurantTabFragment : ReviewableTabFragment(R.layout.fragment_restaurant_tab) {
    private lateinit var restaurantButton: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restaurantButton = view.findViewById<Button?>(R.id.restaurantTabButton).apply {
            setOnClickListener {
                displayContent<RestaurantListActivity>()
            }
        }

    }
}