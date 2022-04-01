package com.github.sdp.ratemyepfl.activity.classrooms

import android.os.Bundle
import androidx.activity.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewableListActivity
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.viewmodel.ClassroomListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassroomsListActivity : ReviewableListActivity<Classroom>() {

    private val viewModel: ClassroomListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.classrooms.observe(this) {
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