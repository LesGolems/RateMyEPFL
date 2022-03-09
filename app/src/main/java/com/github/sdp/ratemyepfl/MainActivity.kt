package com.github.sdp.ratemyepfl

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.auth.UserAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    @Inject lateinit var auth: UserAuth

    private lateinit var mLoginButton: Button
    private lateinit var mLogoutButton: Button
    private lateinit var mEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // We get a reference to the view objects, using their previously set ID
        val mNameText = findViewById<EditText>(R.id.mainName)
        val mGoButton = findViewById<Button>(R.id.mainGoButton)
        val mReviewButton = findViewById<Button>(R.id.coursesReviewButton)

        mEmail = findViewById(R.id.email)
        mLoginButton = findViewById(R.id.loginButton)
        mLogoutButton = findViewById(R.id.logoutButton)
        checkUser()

        // We then set the behaviour of the button
        // It's quite short, so we can leave it here, but as soon as it starts
        // doing more complex stuff, it should be moved to a separate method
        mGoButton.setOnClickListener {
            val name = mNameText.text.toString()
            sayHello(name)
        }

        mReviewButton.setOnClickListener {
            // To be changed once courses are implemented
            startReview()
        }

        // Bonus: trigger the button when the user presses "enter" in the text field
        mNameText.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                mGoButton.performClick()
                true
            }
            false
        }

        setUpButtons()
    }

    override fun onResume() {
        super.onResume()
        checkUser()
    }

    override fun onStart() {
        super.onStart()
        checkUser()
    }

    private fun sayHello(userName: String) {
        val intent = Intent(this, GreetingActivity::class.java)
        intent.putExtra(GreetingActivity.EXTRA_USER_NAME, userName)
        startActivity(intent)
    }

    private fun startReview() {
        val intent = Intent(this, CourseMgtActivity::class.java)
        startActivity(intent)
    }

    private fun setUpButtons(){
        mLoginButton.setOnClickListener {
            auth.signIn(this)
            checkUser()
        }

        mLogoutButton.setOnClickListener {
            auth.signOut(applicationContext).addOnCompleteListener {
                checkUser()
            }
        }
    }

    private fun checkUser(){
        if(!auth.isLoggedIn()){
            mLogoutButton.isEnabled = false
            mLoginButton.isEnabled = true
            mEmail.text = ""
        } else {
            mLogoutButton.isEnabled = true
            mLoginButton.isEnabled = false
            mEmail.text = auth.getEmail()
        }
    }

}