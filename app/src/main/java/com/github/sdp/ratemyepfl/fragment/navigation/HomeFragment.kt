package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.SplashScreen
import com.github.sdp.ratemyepfl.viewmodel.HomeViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var helloUser: TextView
    private lateinit var connectionButton: MaterialButton

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        helloUser = view.findViewById(R.id.homePageHelloUserText)
        connectionButton = view.findViewById(R.id.homePageConnectionFAB)
        setupConnectionButton()
        setupUser()
    }

    private fun setupUser() {
        viewModel.isUserLoggedIn.observe(viewLifecycleOwner) { loggedIn ->
            val icon: Int =
                if (loggedIn) R.drawable.ic_logout_black_24dp else R.drawable.ic_login_black_24dp
            connectionButton.setIconResource(icon)

            connectionButton.text = if (loggedIn) "Logout" else "Login"

            val userName = viewModel.user.getEmail() ?: "visitor"
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
                    // For some reason, the button appears twice
                } else {
                    startActivity(Intent(context, SplashScreen::class.java))
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
}