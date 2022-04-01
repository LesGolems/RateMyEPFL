package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.classrooms.ClassroomsListActivity
import com.github.sdp.ratemyepfl.viewmodel.ClassroomListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClassroomTabFragment : ReviewableTabFragment() {

    private val viewModel: ClassroomListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getItemsAsLiveData().observe(viewLifecycleOwner) {
            it?.let {
                reviewableAdapter.setData(it.toMutableList())
            }
        }
    }

    override fun getMenuString(): Int {
        return R.menu.rooms_options_menu
    }

    override fun getSearchViewString(): Int {
        return R.id.searchView
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_room_review
    }

}