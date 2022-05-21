package com.github.sdp.ratemyepfl.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.service.OccupancyService
import com.github.sdp.ratemyepfl.utils.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : DrawerActivity() {

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
        PermissionUtils.verifyPermissionAndExecute(
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
                R.id.reviewFragment,
                R.id.eventFragment,
                R.id.mapFragment
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
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

