package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.sdp.ratemyepfl.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.mainNavHostContainer
        ) as NavHostFragment

        navController = navHostFragment.navController

        // Setup the bottom navigation
        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.activityMainBottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        // Setup the top level destinations with an ActionBar
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.reviewFragment, R.id.eventFragment, R.id.mapFragment)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}

