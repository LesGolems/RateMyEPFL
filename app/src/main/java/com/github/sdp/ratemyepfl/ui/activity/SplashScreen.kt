package com.github.sdp.ratemyepfl.ui.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.backend.auth.GoogleAuthenticator
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.backend.database.UserRepository
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.user.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    @Inject
    lateinit var auth: GoogleAuthenticator

    @Inject
    lateinit var user: ConnectedUser

    @Inject
    lateinit var repository: UserRepository

    @Inject
    lateinit var imageStorage: Storage<ImageFile>

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
                    repository.register(User(user)).last()
                }
            }
            goToMain()
        }
    }

    private fun getDefaultProfilePicture(): ImageFile {
        val context = this.applicationContext
        val inputStream: InputStream = context.resources.openRawResource(R.raw.blank_profile_picture)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        return ImageFile("default", bitmap)
    }

    private fun goToMain() {
        // Bugfix: we run this synchronously to avoid unexpected behavior
        startActivity(Intent(this, MainActivity::class.java))
    }

}