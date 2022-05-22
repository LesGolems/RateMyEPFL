package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ekn.gruzer.gaugelibrary.ArcGauge
import com.ekn.gruzer.gaugelibrary.Range
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.utils.InfoFragmentUtils.getNumReviewString
import com.github.sdp.ratemyepfl.utils.MapActivityUtils
import com.github.sdp.ratemyepfl.viewmodel.RestaurantInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
Fragment displayed all relevant information for a restaurant
 */
@AndroidEntryPoint
class RestaurantReviewInfoFragment : Fragment(R.layout.fragment_restaurant_review_info) {

    // Gets the shared view model
    private val viewModel by activityViewModels<RestaurantInfoViewModel>()

    private lateinit var occupancyGauge: ArcGauge
    private lateinit var occupancyRating: TextView
    private lateinit var restaurantImage: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.restaurant.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.restaurantName).text = it?.toString()
            view.findViewById<TextView>(R.id.restaurantNumReview).text =
                getNumReviewString(requireContext(), it.numReviews)
            view.findViewById<RatingBar>(R.id.restaurantRatingBar).rating =
                it.grade.toFloat()

            occupancyGauge = view.findViewById(R.id.occupancyGauge)
            occupancyRating = view.findViewById(R.id.occupancyRating)
            val ratio = occupancyMetric(it)
            setupOccupancyUI(ratio)

            restaurantImage = view.findViewById(R.id.restaurantInfoImage)
            it?.let {
                restaurantImage.setImageResource(
                    MapActivityUtils.PHOTO_MAPPING.getOrDefault(it.name, R.raw.arcadie)
                )
            }
        }
    }

    /**
     * Set up the occupancy gauge and rating (i.e. its bounds, colors and text)
     */
    private fun setupOccupancyUI(ratio: Double) {
        val colors = listOf(
            resources.getColor(R.color.green),
            resources.getColor(R.color.yellow),
            resources.getColor(R.color.red)
        )
        setRanges(colors)
        occupancyGauge.minValue = 0.0
        occupancyGauge.maxValue = 100.0
        occupancyGauge.value = ratio

        occupancyGauge.setFormatter {
            it.toInt().toString() + "%"
        }
        setOccupancyText(ratio)
    }

    /**
     * Set the tooltip text displayed during occupancy gauge on click event
     */
    private fun setOccupancyText(ratio: Double) {
        val thirdOf100 = 100.0/3
        when {
            ratio <= thirdOf100 -> {
                occupancyRating.text = getString(R.string.occupancy_clear)
                occupancyRating.setTextColor(resources.getColor(R.color.green))
            }
            ratio <= 2*thirdOf100 -> {
                occupancyRating.text = getString(R.string.occupancy_busy)
                occupancyRating.setTextColor(resources.getColor(R.color.yellow))
            }
            else -> {
                occupancyRating.text = getString(R.string.occupancy_full)
                occupancyRating.setTextColor(resources.getColor(R.color.red))
            }
        }
    }

    /**
     * Set the limits and the color for each range of the gauge
     */
    private fun setRanges(colors: List<Int>) {
        var iteration = 0
        val rangeSize = 100.0 / colors.size
        for (color in colors) {
            val range = Range()
            range.color = color
            val start = rangeSize * iteration
            range.from = start
            range.to = start + rangeSize

            occupancyGauge.addRange(range)
            iteration += 1
        }
    }

    /**
     * Interpolates occupancy to a percentage
     */
    private fun occupancyMetric(restaurant: Restaurant): Double {
        return (restaurant.occupancy * 100.0) / Restaurant.MAX_OCCUPANCY
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateRestaurant()
    }
}