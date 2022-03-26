package com.github.sdp.ratemyepfl.activity.restaurants

import android.os.Bundle
import androidx.activity.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.activity.ReviewableListActivity
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.RestaurantListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantListActivity : ReviewableListActivity<Restaurant>() {

    private val viewModel: RestaurantListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getRestaurants().observe(this) {
            it?.let {
                reviewableAdapter.setData(it as MutableList<Reviewable>)
            }
        }

    }

    override fun getReviewClass(): Class<ReviewActivity> = RestaurantReviewActivity::class.java as Class<ReviewActivity>

    override fun getMenuString(): Int {
        return R.menu.restaurant_options_menu
    }

    override fun getSearchViewString(): Int {
        return R.id.restaurantSearchView
    }

}