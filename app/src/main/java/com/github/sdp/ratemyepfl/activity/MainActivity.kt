package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.Authenticator
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.fragment.navigation.EventFragment
import com.github.sdp.ratemyepfl.fragment.navigation.HomeFragment
import com.github.sdp.ratemyepfl.fragment.navigation.MapFragment
import com.github.sdp.ratemyepfl.fragment.navigation.ReviewFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var user: ConnectedUser

    @Inject
    lateinit var auth: Authenticator

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var mainFragment: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.activityMainBottomNavigationView)
        mainFragment = findViewById(R.id.mainActivityFragmentContainer)

        supportFragmentManager.commit {
            add(R.id.mainActivityFragmentContainer, HomeFragment())
            setReorderingAllowed(true)
        }

        setupNavigation()
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

}

