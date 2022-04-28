package com.github.sdp.ratemyepfl.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.RestaurantListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var mainFragment: FragmentContainerView

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var locationManager: LocationManager
    private val restaurantListViewModel: RestaurantListViewModel by viewModels()
    private val locationPermissionCode = 1

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
            setOf(R.id.homeFragment, R.id.reviewFragment, R.id.eventFragment, R.id.mapFragment, R.id.timetableFragment)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        startLocationService()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    /**
     * Starts the Location Service and binds this Activity as a Listener
     */
    private fun startLocationService() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Check if location permission has been granted
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            // send location updates to this LocationListener
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0f, this)
        }
    }


    /**
     * Receives location updates
     */
    override fun onLocationChanged(location: Location) {
        restaurantListViewModel.updateRestaurantsOccupancy(location)
    }


    /**
     * Pops a little message after the user has specified his location preferences
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Services Enable", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Location Services Disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

