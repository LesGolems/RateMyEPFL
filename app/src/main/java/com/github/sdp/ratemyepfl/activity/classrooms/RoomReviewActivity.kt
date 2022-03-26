package com.github.sdp.ratemyepfl.activity.classrooms

import android.os.Bundle
import androidx.activity.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.viewmodel.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomReviewActivity: ReviewActivity() {

    private val viewModel by viewModels<ReviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_review)

        setUpNavigation(R.id.roomReviewNavigationView, R.id.roomReviewNavHostFragment)
    }
}