package com.github.sdp.ratemyepfl.activity.restaurants

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.AddReviewActivity
import com.github.sdp.ratemyepfl.activity.ReviewableListActivity
import com.github.sdp.ratemyepfl.activity.ReviewsListActivity
import com.github.sdp.ratemyepfl.activity.classrooms.RoomReviewActivity
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.RestaurantListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

    override fun displayReviews(restaurant: Restaurant) {
        val intent = Intent(this,  RestaurantReviewListActivity::class.java as Class<ReviewsListActivity<Restaurant>>)
        intent.putExtra(RestaurantReviewListActivity.EXTRA_RESTAURANT_JSON, Json.encodeToString(restaurant))
        startActivity(intent)
    }

    override fun getMenuString(): Int {
        return R.menu.restaurant_options_menu
    }

    override fun getSearchViewString(): Int {
        return R.id.restaurantSearchView
    }

}