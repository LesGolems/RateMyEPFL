package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.sdp.ratemyepfl.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

/*
General activity for the review part of the app, it has a different layout (navigation) for each reviewable item.
The layout is passed as extra and set in the onCreate method
 */
@AndroidEntryPoint
class ReviewActivity : AppCompatActivity() {

    companion object {
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

        // Note that for simplicity the components id are the same for each activity layout (i.e same id in xml file)
        val bottomNavigationReview = findViewById<BottomNavigationView>(R.id.reviewNavigationView)

        val layout = intent.getIntExtra(EXTRA_MENU_ID, 0)
        bottomNavigationReview.inflateMenu(layout)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.reviewNavHostFragment) as NavHostFragment

        val graphId = intent.getIntExtra(EXTRA_GRAPH_ID, 0)
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(graphId)
        navHostFragment.navController.graph = graph
        bottomNavigationReview.setupWithNavController(navHostFragment.navController)
    }
}