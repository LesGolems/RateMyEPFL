package com.github.sdp.ratemyepfl.activity.classrooms

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.AddReviewActivity
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.activity.ReviewableListActivity
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.ClassroomsListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassroomsListActivity : ReviewableListActivity<Classroom>() {

    private val viewModel: ClassroomsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getRooms().observe(this) {
            it?.let {
                reviewableAdapter.setData(it as MutableList<Reviewable>)
            }
        }
    }

    override fun getReviewClass(): Class<ReviewActivity> = RoomReviewActivity::class.java as Class<ReviewActivity>

    override fun getMenuString(): Int {
        return R.menu.rooms_options_menu
    }

    override fun getSearchViewString(): Int {
        return R.id.searchView
    }
}