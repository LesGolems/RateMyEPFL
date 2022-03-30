package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.sdp.ratemyepfl.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ITEM_REVIEWED: String =
            "com.github.sdp.extra_item_reviewed"

        const val EXTRA_LAYOUT_ID: String =
            "com.github.sdp.extra_layout_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = intent.getIntExtra(EXTRA_LAYOUT_ID, 0)
        setContentView(layout)

        val bottomNavigationReview = findViewById<BottomNavigationView>(R.id.reviewNavigationView)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.reviewNavHostFragment) as NavHostFragment
        bottomNavigationReview.setupWithNavController(navHostFragment.navController)
    }
}