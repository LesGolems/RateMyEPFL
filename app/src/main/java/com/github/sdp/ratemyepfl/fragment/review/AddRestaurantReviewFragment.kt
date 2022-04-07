package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.RestaurantInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRestaurantReviewFragment : AddReviewFragment() {
    private lateinit var reviewIndicationTitle: TextView

    private val restaurantReviewViewModel: RestaurantInfoViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reviewIndicationTitle = view.findViewById(R.id.reviewTitle)

        reviewIndicationTitle.text = getString(R.string.title_review, restaurantReviewViewModel.id)
    }

    override fun submitReview(): Boolean {
        val rating = addReviewViewModel.submitReview(restaurantReviewViewModel.id) ?: return false
        restaurantReviewViewModel.updateRating(rating)
        return true
    }
}