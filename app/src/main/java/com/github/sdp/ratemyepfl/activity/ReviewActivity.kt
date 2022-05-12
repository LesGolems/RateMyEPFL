package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.sdp.ratemyepfl.R
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

        const val EXTRA_MENU_ID: String =
            "com.github.sdp.extra_menu_id"

        const val EXTRA_GRAPH_ID: String =
            "com.github.sdp.extra_graph_id"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.reviewNavHostFragment) as NavHostFragment

        navController = navHostFragment.navController

        drawerLayout = findViewById(R.id.reviewActivityDrawerLayout)
        drawerView = findViewById(R.id.drawerView)
        bottomNavigationView =
            findViewById(R.id.reviewBottomNavigationView)

        val layout = intent.getIntExtra(EXTRA_MENU_ID, 0)
        bottomNavigationView.inflateMenu(layout)

        val graphId = intent.getIntExtra(EXTRA_GRAPH_ID, 0)
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(graphId)
        navHostFragment.navController.graph = graph

        setUpBottomNavigation()
        setUpDrawerNavigation()
        setUpProfile()
        setUpLoginLogout()
    }

    private fun setUpDrawerNavigation() {
        drawerView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.reviewListFragment,
                R.id.roomReviewInfoFragment,
                R.id.roomReviewPictureFragment,
                R.id.addReviewFragment,
                R.id.restaurantReviewInfoFragment,
                R.id.eventReviewInfoFragment,
                R.id.courseReviewInfoFragment,
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}