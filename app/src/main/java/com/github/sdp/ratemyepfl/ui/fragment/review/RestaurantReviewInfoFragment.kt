package com.github.sdp.ratemyepfl.ui.fragment.review

import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ekn.gruzer.gaugelibrary.ArcGauge
import com.ekn.gruzer.gaugelibrary.Range
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.ui.layout.LoadingImageView
import com.github.sdp.ratemyepfl.utils.InfoFragmentUtils.getNumReviewString
import com.github.sdp.ratemyepfl.utils.MapActivityUtils
import com.github.sdp.ratemyepfl.viewmodel.review.RestaurantInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
Fragment displayed all relevant information for a restaurant
 */
@AndroidEntryPoint
class RestaurantReviewInfoFragment : Fragment(R.layout.fragment_restaurant_review_info) {

    // Gets the shared view model
    private val viewModel by activityViewModels<RestaurantInfoViewModel>()

    private lateinit var restaurantName: TextView
    private lateinit var restaurantNumReview: TextView
    private lateinit var restaurantRatingBar: RatingBar
    private lateinit var occupancyGauge: ArcGauge
    private lateinit var occupancyRating: TextView
    private lateinit var restaurantImage: LoadingImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restaurantName = view.findViewById(R.id.restaurantName)
        restaurantNumReview = view.findViewById(R.id.restaurantNumReview)
        restaurantRatingBar = view.findViewById(R.id.restaurantRatingBar)
        occupancyGauge = view.findViewById(R.id.occupancyGauge)
        occupancyRating = view.findViewById(R.id.occupancyRating)
        val image: View = view.findViewById(R.id.restaurantInfoImageLayout)
        restaurantImage = LoadingImageView(image, R.raw.arcadie)


        viewModel.restaurant.observe(viewLifecycleOwner) {
            setUpRestaurantInfo(it)
        }
    }

    private fun setUpRestaurantInfo(restaurant: Restaurant) {
        restaurantName.text = restaurant.toString()
        restaurantNumReview.text = getNumReviewString(requireContext(), restaurant.numReviews)
        restaurantRatingBar.rating = restaurant.grade.toFloat()

        val ratio = occupancyMetric(restaurant)
        setupOccupancyUI(ratio)

        restaurantImage.image
            .setImageResource(
                MapActivityUtils.PHOTO_MAPPING.getOrDefault(restaurant.name, R.raw.arcadie)
            )
    }

    /**
     * Set up the occupancy gauge and rating (i.e. its bounds, colors and text)
     */
    private fun setupOccupancyUI(ratio: Double) {
        val context = requireContext()
        val colors = listOf(
            ContextCompat.getColor(context, R.color.green),
            ContextCompat.getColor(context, R.color.orange),
            ContextCompat.getColor(context, R.color.red)
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
        val thirdOf100 = 100.0 / 3
        val context = requireContext()
        when {
            ratio <= thirdOf100 -> {
                occupancyRating.text = getString(R.string.occupancy_clear)
                occupancyRating.setTextColor(ContextCompat.getColor(context, R.color.green))
            }
            ratio <= 2 * thirdOf100 -> {
                occupancyRating.text = getString(R.string.occupancy_busy)
                occupancyRating.setTextColor(ContextCompat.getColor(context, R.color.orange))
            }
            else -> {
                occupancyRating.text = getString(R.string.occupancy_full)
                occupancyRating.setTextColor(ContextCompat.getColor(context, R.color.red))
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