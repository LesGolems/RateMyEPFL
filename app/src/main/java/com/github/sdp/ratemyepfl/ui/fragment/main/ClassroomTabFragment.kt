package com.github.sdp.ratemyepfl.ui.fragment.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.viewmodel.filter.ClassroomFilter
import com.github.sdp.ratemyepfl.viewmodel.filter.ReviewableFilter
import com.github.sdp.ratemyepfl.viewmodel.main.ClassroomListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class ClassroomTabFragment : ReviewableTabFragment<Classroom>(R.menu.restaurant_options_menu) {

    override val viewModel: ClassroomListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.elements
            .observe(viewLifecycleOwner) { classrooms ->
                reviewableAdapter.submitData(classrooms)
            }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun alphabeticFilter(reverse: Boolean): ReviewableFilter<Classroom> =
        if (!reverse) ClassroomFilter.AlphabeticalOrder else ClassroomFilter.AlphabeticalOrderReversed

    override fun bestRatedFilter(): ReviewableFilter<Classroom> = ClassroomFilter.BestRated

    override fun worstRatedFilter(): ReviewableFilter<Classroom> = ClassroomFilter.WorstRated
}