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

    override val reviewActivityLayoutId: Int = R.layout.activity_restaurant_review

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