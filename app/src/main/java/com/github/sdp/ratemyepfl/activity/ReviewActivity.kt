package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.ReviewViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_ITEM_REVIEWED: String =
            "com.github.sdp.activity.classrooms.extra_item_reviewed"
    }

    private val viewModel by viewModels<ReviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up the corresponding layout and navigation depending on the reviewed item type
        viewModel.getReviewable().observe(this){
            if(it != null){
                setContentView(viewModel.getReviewable().value!!.layoutReview)
                val bottomNavigationReview = findViewById<BottomNavigationView>(R.id.reviewNavigationView)
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.reviewNavHostFragment) as NavHostFragment
                bottomNavigationReview.setupWithNavController(navHostFragment.navController)
            }
        }
    }
}