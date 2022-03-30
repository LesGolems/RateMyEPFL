package com.github.sdp.ratemyepfl.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.map.MapActivity
import com.github.sdp.ratemyepfl.auth.Authenticator
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.github.sdp.ratemyepfl.fragment.navigation.EventFragment
import com.github.sdp.ratemyepfl.fragment.navigation.HomeFragment
import com.github.sdp.ratemyepfl.fragment.navigation.MapFragment
import com.github.sdp.ratemyepfl.fragment.navigation.ReviewFragment
import com.github.sdp.ratemyepfl.viewmodel.RestaurantListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var mainFragment: FragmentContainerView

    private lateinit var locationManager: LocationManager
    private val restaurantListViewModel: RestaurantListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.activityMainBottomNavigationView)
        mainFragment = findViewById(R.id.mainActivityFragmentContainer)

        setupNavigation()
        startLocationService()
    }

    /**
     * Setup the listener for the bottom navigation menu
     */
    private fun setupNavigation() {
        bottomNavigation.setOnItemSelectedListener { item ->
            manageNavigation(item.itemId)
        }

        // When a item is reselected, it refreshes the fragment
        bottomNavigation.setOnItemReselectedListener { item ->
            manageNavigation(item.itemId)
        }

    }

    /**
     * Perform the replacement of fragment when the user presses
     * one of the navigation item
     *
     * @param id: id of the navigation item
     */
    private fun manageNavigation(id: Int) =
        when (id) {
            R.id.homeNavItem -> swapFragment<HomeFragment>("nav")
            R.id.reviewNavItem -> swapFragment<ReviewFragment>("nav")
            R.id.eventNavItem -> swapFragment<EventFragment>("nav")
            R.id.mapNavItem -> swapFragment<MapFragment>("nav")
            else -> false
        }

    private inline fun <reified T : Fragment> swapFragment(transactionName: String?): Boolean =
        swapFragment<T>(R.id.mainActivityFragmentContainer, transactionName)

    /**
     * Generic function that replaces the main fragment
     *
     * @param T: Fragment class to put
     * @param fragmentId: id of the fragment whose content is replaced
     * @param transactionName: name of the transaction in the backstack
     */
    private inline fun <reified T : Fragment> swapFragment(
        fragmentId: Int,
        transactionName: String?
    ): Boolean {
        var result: Boolean = false
        supportFragmentManager.commit {
            replace<T>(fragmentId)
            setReorderingAllowed(true)
            addToBackStack(transactionName)
            result = !isEmpty
        }
        return result
    }

    private fun startLocationService(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Check if location permission has been granted
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
        }
        // send location updates to this LocationListener
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    /**
     * Receives location updates
     */
    override fun onLocationChanged(location: Location) {
        restaurantListViewModel.postRestaurantsOccupancy(location)
    }
}

