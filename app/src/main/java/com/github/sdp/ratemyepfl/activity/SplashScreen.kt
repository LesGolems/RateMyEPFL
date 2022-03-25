package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.Authenticator
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.auth.ConnectedUserImpl
import com.github.sdp.ratemyepfl.database.UserDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.lifecycleScope
import com.github.sdp.ratemyepfl.model.user.User

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    @Inject
    lateinit var auth: Authenticator

    @Inject
    lateinit var user: ConnectedUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        val mLoginButton = findViewById<Button>(R.id.loginButton)
        val mVisitorButton = findViewById<Button>(R.id.visitorButton)

        mLoginButton.setOnClickListener {
            auth.signIn(this)
        }

        mVisitorButton.setOnClickListener {
            goToMain()
        }
    }

    override fun onResume() {
        super.onResume()
        if (user.isLoggedIn()) goToMain()
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}