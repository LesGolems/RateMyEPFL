package com.github.sdp.ratemyepfl.ui.fragment.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.viewmodel.filter.RestaurantFilter
import com.github.sdp.ratemyepfl.viewmodel.filter.ReviewableFilter
import com.github.sdp.ratemyepfl.viewmodel.main.RestaurantListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantTabFragment : ReviewableTabFragment<Restaurant>(R.menu.restaurant_options_menu) {

    override val viewModel: RestaurantListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.elements
            .observe(viewLifecycleOwner) { restaurants ->
                reviewableAdapter.submitData(restaurants)
            }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun alphabeticFilter(reverse: Boolean): ReviewableFilter<Restaurant> =
        if (!reverse) RestaurantFilter.AlphabeticalOrder else RestaurantFilter.AlphabeticalOrderReversed

    override fun bestRatedFilter(): ReviewableFilter<Restaurant> = RestaurantFilter.BestRated

    override fun worstRatedFilter(): ReviewableFilter<Restaurant> = RestaurantFilter.WorstRated

}