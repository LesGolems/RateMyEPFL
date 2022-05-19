package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.FragmentViewPagerAdapter
import com.github.sdp.ratemyepfl.fragment.review.ReviewListFragment
import com.github.sdp.ratemyepfl.fragment.review.RoomReviewInfoFragment
import com.github.sdp.ratemyepfl.model.serializer.getReviewable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/*
General activity for the review part of the app, it has a different layout (navigation) for each reviewable item.
The layout is passed as extra and set in the onCreate method
 */
@AndroidEntryPoint
class ReviewActivity : DrawerActivity() {

    companion object {
        const val EXTRA_ITEM_REVIEWED_ID: String =
            "com.github.sdp.extra_item_reviewed_id"

        const val EXTRA_ITEM_REVIEWED: String =
            "com.github.sdp.extra_item_reviewed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.reviewNavHostContainer
        ) as NavHostFragment

        navController = navHostFragment.navController
        drawerLayout = findViewById(R.id.reviewActivityDrawerLayout)
        drawerView = findViewById(R.id.drawerView)

        setUpDrawerNavigation()
        setUpProfile()
        setUpLoginLogout()
    }

    private fun setUpDrawerNavigation() {
        drawerView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.reviewFragment
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}