package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.Authenticator
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    @Inject lateinit var auth : Authenticator
    @Inject lateinit var user : ConnectedUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        val mLoginButton = findViewById<Button>(R.id.login_button)
        val mVisitorButton = findViewById<Button>(R.id.visitor_button)

        mLoginButton.setOnClickListener {
            auth.signIn(this)
        }

        mVisitorButton.setOnClickListener {
            goToMain()
        }
    }

    override fun onResume() {
        super.onResume()
        if(user.isLoggedIn()) goToMain()
    }

    private fun goToMain(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}