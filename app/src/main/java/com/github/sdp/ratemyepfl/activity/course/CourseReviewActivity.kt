package com.github.sdp.ratemyepfl.activity.course

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.CourseReviewViewModel
import com.github.sdp.ratemyepfl.viewmodel.RoomReviewViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseReviewActivity: AppCompatActivity() {

    private val viewModel by viewModels<CourseReviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_review)

        setUpNavigation()
    }

    private fun setUpNavigation(){
        val bottomNavigationReview = findViewById<BottomNavigationView>(R.id.courseReviewNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.courseReviewNavHostFragment) as NavHostFragment
        bottomNavigationReview.setupWithNavController(navHostFragment.navController)
    }
}