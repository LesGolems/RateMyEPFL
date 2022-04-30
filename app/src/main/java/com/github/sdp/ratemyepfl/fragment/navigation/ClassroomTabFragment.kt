package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.ClassroomListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassroomTabFragment : ReviewableTabFragment() {

    private val viewModel: ClassroomListViewModel by viewModels()

    override val reviewActivityMenuId: Int = R.menu.bottom_navigation_menu_room_review

    override val reviewActivityGraphId: Int = R.navigation.nav_graph_room_review

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.classrooms
            .observe(viewLifecycleOwner) { classrooms ->
                reviewableAdapter.submitData(classrooms)
            }
    }

    override fun onResume() {
        // BUGFIX
        viewModel.classrooms.postValue(viewModel.classrooms.value ?: listOf())
        super.onResume()
    }
}