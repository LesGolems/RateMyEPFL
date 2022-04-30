package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.UserProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
open class DrawerActivity : AppCompatActivity() {
    protected lateinit var navController: NavController
    protected lateinit var appBarConfiguration: AppBarConfiguration
    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var bottomNavigationView: BottomNavigationView
    protected lateinit var drawerView: NavigationView

    private val profileViewModel: UserProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    private fun showBottomNav() {
        bottomNavigationView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        bottomNavigationView.visibility = View.GONE
    }

    /**
     * Setups the bottom navigation, when on user profile the bottom bar is not shown
     */
    protected fun setUpBottomNavigation() {
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.profileFragment -> hideBottomNav()
                R.id.timetableFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    protected fun setUpProfile() {
        val headerView = drawerView.getHeaderView(0)
        val pic: CircleImageView = headerView.findViewById(R.id.pictureDrawer)
        val username: TextView = headerView.findViewById(R.id.usernameDrawer)
        val email: TextView = headerView.findViewById(R.id.emailDrawer)

        profileViewModel.picture.observe(this) {
            pic.setImageBitmap(it?.data)
        }
        profileViewModel.username.observe(this) {
            username.text = it
        }
        profileViewModel.email.observe(this) {
            email.text = it
        }
    }

    protected fun setUpLoginLogout() {
        profileViewModel.isUserLoggedIn.observe(this) { loggedIn ->
            if (loggedIn) {
                drawerView.menu.findItem(R.id.login).isVisible = false
                drawerView.menu.findItem(R.id.logout).isVisible = true
            } else {
                drawerView.menu.findItem(R.id.login).isVisible = true
                drawerView.menu.findItem(R.id.logout).isVisible = false
            }
        }

        drawerView.menu.findItem(R.id.login).setOnMenuItemClickListener {
            startActivity(Intent(this, SplashScreen::class.java))
            true
        }

        drawerView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            profileViewModel.signOut(this)
            true
        }
    }

}

