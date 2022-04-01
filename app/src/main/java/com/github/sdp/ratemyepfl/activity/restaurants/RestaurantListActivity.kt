package com.github.sdp.ratemyepfl.activity.restaurants

import android.os.Bundle
import androidx.activity.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewableListActivity
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.viewmodel.RestaurantListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantListActivity : ReviewableListActivity<Restaurant>() {

    private val viewModel: RestaurantListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getItemsAsLiveData().observe(this) {
            it?.let {
                reviewableAdapter.setData(it.toMutableList())
            }
        }

    }

    override fun getMenuString(): Int {
        return R.menu.default_filter_menu
    }

    override fun getSearchViewString(): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_restaurant_review
    }

}