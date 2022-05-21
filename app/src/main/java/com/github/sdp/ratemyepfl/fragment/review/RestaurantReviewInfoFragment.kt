package com.github.sdp.ratemyepfl.fragment.review

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.utils.InfoFragmentUtils.getNumReviewString
import com.github.sdp.ratemyepfl.viewmodel.review.RestaurantInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
Fragment displayed all relevant information for a restaurant
 */
@AndroidEntryPoint
class RestaurantReviewInfoFragment : Fragment(R.layout.fragment_restaurant_review_info) {

    // Gets the shared view model
    private val viewModel by activityViewModels<RestaurantInfoViewModel>()

    private lateinit var occupancyBar: RatingBar
    private lateinit var restaurantIdInfo: TextView
    private lateinit var restaurantNumReview: TextView
    private lateinit var restaurantRatingBar: RatingBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restaurantIdInfo = view.findViewById(R.id.restaurantIdInfo)
        restaurantNumReview = view.findViewById(R.id.restaurantNumReview)
        restaurantRatingBar = view.findViewById(R.id.restaurantRatingBar)
        occupancyBar = view.findViewById(R.id.occupancyMetric)

        viewModel.restaurant.observe(viewLifecycleOwner) {
            restaurantIdInfo.text = it?.toString()
            restaurantNumReview.text =
                getNumReviewString(requireContext(), it.numReviews)
            restaurantRatingBar.rating =
                it.grade.toFloat()
            val n = occupancyMetric(it)
            setupOccupancyUI(view, n)
        }
    }

    private fun setupOccupancyUI(view: View, n: Int) {
        occupancyBar.progressTintList = when (n) {
            1 -> ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green))
            2 -> ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.yellow))
            3 -> ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
            4 -> ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.dark_orange
                )
            )
            else -> ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red))
        }
        occupancyBar.rating = n.toFloat()
        view.findViewById<TextView>(R.id.occupancyRating).text = when {
            n <= 2 -> "Clear"
            n <= 4 -> "Busy"
            else -> "Full"
        }
    }


    /**
     * Interpolates occupancy to a ratio between 1 and 5
     */
    private fun occupancyMetric(restaurant: Restaurant): Int {
        val ratio = Restaurant.MAX_OCCUPANCY / 5
        val n = restaurant.occupancy / ratio
        return 1 + n
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateRestaurant()
    }
}