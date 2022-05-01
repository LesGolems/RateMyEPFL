package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.RestaurantListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantTabFragment : ReviewableTabFragment() {

    private val viewModel: RestaurantListViewModel by viewModels()

    override val reviewActivityMenuId: Int = R.menu.bottom_navigation_menu_restaurant_review

    override val reviewActivityGraphId: Int = R.navigation.nav_graph_restaurant_review

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.restaurants.observe(viewLifecycleOwner) { restaurants ->
            reviewableAdapter.submitData(restaurants)
        }
    }

    override fun onResume() {
        // BUGFIX
        viewModel.restaurants.postValue(viewModel.restaurants.value ?: listOf())
        super.onResume()
    }

}