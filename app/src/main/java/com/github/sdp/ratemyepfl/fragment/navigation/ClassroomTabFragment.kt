package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.ClassroomListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassroomTabFragment : ReviewableTabFragment() {

    private val viewModel: ClassroomListViewModel by viewModels()

    override val reviewActivityLayoutId: Int = R.layout.activity_room_review

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.classrooms.observe(viewLifecycleOwner) { classrooms ->
            reviewableAdapter.setData(classrooms)
        }
    }
}