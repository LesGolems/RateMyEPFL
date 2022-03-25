package com.github.sdp.ratemyepfl.activity.course

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.viewmodel.CourseReviewViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseReviewActivity: ReviewActivity() {

    private val viewModel by viewModels<CourseReviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_review)

        setUpNavigation(R.id.courseReviewNavigationView, R.id.courseReviewNavHostFragment)
    }
}