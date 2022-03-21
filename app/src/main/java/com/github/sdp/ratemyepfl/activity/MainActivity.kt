package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.course.CourseListActivity
import com.github.sdp.ratemyepfl.auth.Authenticator
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.fragment.EventFragment
import com.github.sdp.ratemyepfl.fragment.HomeFragment
import com.github.sdp.ratemyepfl.fragment.MapFragment
import com.github.sdp.ratemyepfl.fragment.ReviewFragment
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
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

    override fun onResume() {
        super.onResume()
        checkUser()
    }


    override fun onStart() {
        super.onStart()
        checkUser()
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
            R.id.homeNavItem -> swapFragment<HomeFragment>("home")
            R.id.reviewNavItem -> swapFragment<ReviewFragment>("review")
            R.id.eventNavItem -> swapFragment<EventFragment>("event")
            R.id.mapNavItem -> swapFragment<MapFragment>("map")
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

    private fun displayCourses() {
        val intent = Intent(this, CourseListActivity::class.java)
        startActivity(intent)
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

    private fun checkUser() {
        /*
        if (!user.isLoggedIn()) {
            mLogoutButton.isEnabled = false
            mUser_text.text = getString(R.string.visitor)
        } else {
            mLogoutButton.isEnabled = true
            mUser_text.text = user.getEmail()
        }

         */
    }
}

