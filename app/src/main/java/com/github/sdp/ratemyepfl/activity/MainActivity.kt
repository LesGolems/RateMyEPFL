package com.github.sdp.ratemyepfl.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.utils.PermissionUtils
import com.github.sdp.ratemyepfl.viewmodel.RestaurantListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : DrawerActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private val restaurantListViewModel: RestaurantListViewModel by viewModels()

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
            PermissionUtils.requestPermissionLauncher({ startLocationService() }, this, this)
        PermissionUtils.startPhoneFeature(
            { startLocationService() },
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
     * Starts the Location Service and binds this Activity as a Listener
     */
    @SuppressLint("MissingPermission")
    private fun startLocationService() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // send location updates to this LocationListener
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0f, this)
    }

    /**
     * Receives location updates
     */
    override fun onLocationChanged(location: Location) {
        restaurantListViewModel.updateRestaurantsOccupancy(location)
    }
}

