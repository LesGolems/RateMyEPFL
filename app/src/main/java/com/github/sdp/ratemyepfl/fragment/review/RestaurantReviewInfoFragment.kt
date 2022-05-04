package com.github.sdp.ratemyepfl.fragment.review

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.utils.InfoFragmentUtils.getNumReviewString
import com.github.sdp.ratemyepfl.viewmodel.RestaurantInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
Fragment displayed all relevant information for a restaurant
 */
@AndroidEntryPoint
class RestaurantReviewInfoFragment : Fragment(R.layout.fragment_restaurant_review_info) {

    // Gets the shared view model
    private val viewModel by activityViewModels<RestaurantInfoViewModel>()

    private lateinit var occupancyBar: RatingBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.restaurant.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.restaurantIdInfo).text = it?.toString()
            view.findViewById<TextView>(R.id.restaurantNumReview).text =
                getNumReviewString(requireContext(), it.numReviews)
            view.findViewById<RatingBar>(R.id.restaurantRatingBar).rating =
                it.averageGrade.toFloat()
            occupancyBar = view.findViewById(R.id.occupancyMetric)
            val n = occupancyMetric(it)
            setupOccupancyUI(view, n)
        }
    }

    private fun setupOccupancyUI(view: View, n: Int) {
        occupancyBar.progressTintList = when {
            n == 1 -> ColorStateList.valueOf(resources.getColor(R.color.green))
            n == 2 -> ColorStateList.valueOf(resources.getColor(R.color.yellow))
            n == 3 -> ColorStateList.valueOf(resources.getColor(R.color.orange))
            n == 4 -> ColorStateList.valueOf(resources.getColor(R.color.dark_orange))
            else -> ColorStateList.valueOf(resources.getColor(R.color.red))
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
        viewModel.refresh()
    }
}