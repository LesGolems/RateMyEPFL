package com.github.sdp.ratemyepfl.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.Authenticator
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    private val resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                goToMain()
            }
        }

    @Inject lateinit var auth : Authenticator
    @Inject lateinit var user : ConnectedUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        if(user.isLoggedIn()) goToMain()

        val button = findViewById<Button>(R.id.login_button)

        button.setOnClickListener {
            auth.signIn(resultLauncher)
        }
    }

    private fun goToMain(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}