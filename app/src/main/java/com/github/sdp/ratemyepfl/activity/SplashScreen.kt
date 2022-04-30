package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.auth.GoogleAuthenticator
import com.github.sdp.ratemyepfl.database.DatabaseException
import com.github.sdp.ratemyepfl.database.UserRepository
import com.github.sdp.ratemyepfl.database.UserRepositoryImpl
import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.model.user.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    @Inject
    lateinit var auth: GoogleAuthenticator

    @Inject
    lateinit var user: ConnectedUser

    @Inject
    lateinit var repository: UserRepository

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
        if (user.isLoggedIn()) {
            runBlocking {
                launch(Dispatchers.IO) {
                    repository.register(User(user)).await()
                }
            }
            goToMain()
        }
    }

    private fun goToMain() {
        // Bugfix: we run this synchronously to avoid unexpected behavior
        startActivity(Intent(this, MainActivity::class.java))
    }

}