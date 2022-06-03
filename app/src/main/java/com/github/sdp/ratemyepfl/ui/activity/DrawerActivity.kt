package com.github.sdp.ratemyepfl.ui.activity

import android.content.Intent
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.profile.UserViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
open class DrawerActivity : AppCompatActivity() {
    protected lateinit var navController: NavController
    protected lateinit var appBarConfiguration: AppBarConfiguration
    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var drawerView: NavigationView

    private val userViewModel: UserViewModel by viewModels()

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    protected fun setUpProfile() {
        val headerView = drawerView.getHeaderView(0)
        val pic: CircleImageView = headerView.findViewById(R.id.pictureDrawer)
        val username: TextView = headerView.findViewById(R.id.usernameDrawer)
        val email: TextView = headerView.findViewById(R.id.emailDrawer)
        val karmaCount: TextView = headerView.findViewById(R.id.karmaCount)
        val golemLayout: ConstraintLayout = headerView.findViewById(R.id.coinDisplay)

        userViewModel.isUserLoggedIn.observe(this) {
            golemLayout.isVisible = it
        }
        userViewModel.user.observe(this) {
            if (it != null) {
                karmaCount.text = it.karma.toString()
                username.text = it.username
                email.text = it.email
            } else {
                username.text = getString(R.string.visitor)
                email.text = getString(R.string.not_logged_in_text)
            }
        }

        userViewModel.picture.observe(this) {
            pic.setImageBitmap(it?.data)
        }
    }

    /**
     * Display login/logout button if user is logged in/logged out
     */
    protected fun setUpLoginLogout() {
        userViewModel.isUserLoggedIn.observe(this) { loggedIn ->
            drawerView.menu.findItem(R.id.login).isVisible = !(loggedIn)
            drawerView.menu.findItem(R.id.logout).isVisible = loggedIn
            drawerView.menu.findItem(R.id.timetable).isVisible = loggedIn
            drawerView.menu.findItem(R.id.profile).isVisible = loggedIn
        }

        drawerView.menu.findItem(R.id.login).setOnMenuItemClickListener {
            startActivity(Intent(this, SplashScreen::class.java))
            true
        }

        drawerView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            userViewModel.signOut(this)
            true
        }
    }
}

