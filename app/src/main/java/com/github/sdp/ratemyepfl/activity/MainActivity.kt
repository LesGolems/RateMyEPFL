package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.course.CourseListActivity
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
        mainFragment = findViewById(R.id.mainActivityFragment)

        supportFragmentManager.commit {
            add(R.id.mainActivityFragment, HomeFragment())
            setReorderingAllowed(true)
        }

        setupNavigation()
    }

    private fun setupNavigation() {

        // Setup the size of the icons

        bottomNavigation.setOnItemSelectedListener { item ->
            manageNavigation(item.itemId)
        }

        // When a item is reselected, it refreshes the fragment
        bottomNavigation.setOnItemReselectedListener { item ->
            manageNavigation(item.itemId)
        }

    }



    private fun manageNavigation(id: Int) =
        when (id) {
            R.id.homeNavItem -> swapFragment<HomeFragment>("nav")
            R.id.reviewNavItem -> swapFragment<ReviewFragment>("nav")
            R.id.eventNavItem -> swapFragment<EventFragment>("nav")
            R.id.mapNavItem -> swapFragment<MapFragment>("nav")
            else -> false
        }

    private inline fun <reified T : Fragment> swapFragment(transactionName: String?): Boolean =
        swapFragment<T>(R.id.mainActivityFragment, transactionName)

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



    /*
    private fun setUpButtons() {
        mLogoutButton.setOnClickListener {
            auth.signOut(applicationContext).addOnCompleteListener {
                startActivity(Intent(this, SplashScreen::class.java))
            }
        }

        mCoursesButton.setOnClickListener {
            displayCourses()
        }

    }

     */

}

