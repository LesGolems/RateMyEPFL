package com.github.sdp.ratemyepfl.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.sdp.ratemyepfl.OccupancyService
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.utils.PermissionUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : DrawerActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.mainNavHostContainer
        ) as NavHostFragment

        navController = navHostFragment.navController
        drawerLayout = findViewById(R.id.mainActivityDrawerLayout)
        drawerView = findViewById(R.id.drawerView)
        bottomNavigationView = findViewById(R.id.activityMainBottomNavigationView)

        setUpBottomNavigation()
        setUpDrawerNavigation()
        setUpProfile()
        setUpLoginLogout()

        // Start location service
        val locationPermissionLauncher =
            PermissionUtils.requestPermissionLauncher({ startOccupancyService() }, this, this)
        PermissionUtils.startPhoneFeature(
            { startOccupancyService() },
            locationPermissionLauncher,
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun setUpDrawerNavigation() {
        drawerView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.reviewableFragment,
                R.id.eventFragment,
                R.id.mapFragment
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
    }


    /**
     * Setups the bottom navigation, when on user profile the bottom bar is not shown
     */
    private fun setUpBottomNavigation() {
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.profileFragment -> hideBottomNav()
                R.id.timetableFragment -> hideBottomNav()
                R.id.addClassFragment -> hideBottomNav()
                R.id.selectCourseFragment -> hideBottomNav()
                R.id.selectRoomFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        bottomNavigationView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        bottomNavigationView.visibility = View.GONE
    }

    /**
     * Starts the Occupancy Service
     */
    private fun startOccupancyService() {
        Intent(this, OccupancyService::class.java).also {
            startService(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Intent(this, OccupancyService::class.java).also {
            stopService(it)
        }
    }
}

