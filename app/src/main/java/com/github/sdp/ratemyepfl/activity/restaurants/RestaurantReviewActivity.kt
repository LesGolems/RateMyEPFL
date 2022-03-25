package com.github.sdp.ratemyepfl.activity.restaurants

import android.os.Bundle
import androidx.activity.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.viewmodel.CourseReviewViewModel
import com.github.sdp.ratemyepfl.viewmodel.RestaurantReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantReviewActivity: ReviewActivity() {

    private val viewModel by viewModels<RestaurantReviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_review)

        setUpNavigation(R.id.restaurantReviewNavigationView, R.id.restaurantReviewNavHostFragment)
    }
}