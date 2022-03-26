package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.sdp.ratemyepfl.R
import com.google.android.material.bottomnavigation.BottomNavigationView

open class ReviewActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_ITEM_REVIEWED: String =
            "com.github.sdp.activity.classrooms.extra_item_reviewed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setUpNavigation(bottomNavigationId : Int, navHostId : Int){
        val bottomNavigationReview = findViewById<BottomNavigationView>(bottomNavigationId)
        val navHostFragment = supportFragmentManager.findFragmentById(navHostId) as NavHostFragment
        bottomNavigationReview.setupWithNavController(navHostFragment.navController)
    }
}