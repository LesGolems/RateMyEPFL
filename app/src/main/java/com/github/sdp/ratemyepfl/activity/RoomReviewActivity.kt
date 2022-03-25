package com.github.sdp.ratemyepfl.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.RoomReviewViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomReviewActivity: AppCompatActivity() {
    private lateinit var bottomNavigationReview: BottomNavigationView

    private val viewModel by viewModels<RoomReviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_review)

        bottomNavigationReview = findViewById(R.id.roomReviewNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.roomReviewNavHostFragment) as NavHostFragment
        bottomNavigationReview.setupWithNavController(navHostFragment.navController)
    }
}