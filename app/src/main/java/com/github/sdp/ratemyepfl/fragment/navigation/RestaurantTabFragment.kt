package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.RestaurantListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantTabFragment : ReviewableTabFragment(R.layout.fragment_restaurant_tab) {

    private val viewModel: RestaurantListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getItemsAsLiveData().observe(viewLifecycleOwner) {
            it?.let {
                reviewableAdapter.setData(it.toMutableList())
            }
        }

    }

    override fun getMenuString(): Int {
        return R.menu.restaurant_options_menu
    }

    override fun getSearchViewString(): Int {
        return R.id.restaurantSearchView
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_restaurant_review
    }
}