package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.SplashScreen
import com.github.sdp.ratemyepfl.activity.UserProfileActivity
import com.github.sdp.ratemyepfl.viewmodel.HomeViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var helloUser: TextView
    private lateinit var connectionButton: MaterialButton
    private lateinit var userProfileButton: MaterialButton

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        helloUser = view.findViewById(R.id.homePageHelloUserText)
        connectionButton = view.findViewById(R.id.homePageConnectionButton)
        userProfileButton = view.findViewById(R.id.user_profile_button)

        setupUserProfileButton()
        setupConnectionButton()
        setupUser()
    }


    private fun setupUser() {
        viewModel.isUserLoggedIn.observe(viewLifecycleOwner) { loggedIn ->
            val icon: Int =
                if (loggedIn) R.drawable.ic_logout_black_24dp else R.drawable.ic_login_black_24dp
            connectionButton.setIconResource(icon)

            connectionButton.text = if (loggedIn) LOGOUT else LOGIN

            val userName = viewModel.user.getEmail() ?: VISITOR_NAME
            helloUser.text = getString(R.string.home_page_hello_user_text, userName)
        }
    }

    private fun checkUser() {
        viewModel.updateLoginStatus()
    }

    private fun setupConnectionButton() {
        connectionButton.setOnClickListener {
            if (context != null) {
                if (viewModel.isUserLoggedIn()) {
                    viewModel.signOut(requireContext())
                } else {
                    startActivity(Intent(context, SplashScreen::class.java))
                }
            }
        }
    }

    private fun setupUserProfileButton() {
        userProfileButton.setOnClickListener {
            if (context != null) {
                if (viewModel.isUserLoggedIn()) {
                    startActivity(Intent(context, UserProfileActivity::class.java))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkUser()
    }


    override fun onStart() {
        super.onStart()
        checkUser()
    }

    companion object {
        const val LOGOUT = "Logout"
        const val LOGIN = "Login"
        const val VISITOR_NAME = "visitor"
    }
}