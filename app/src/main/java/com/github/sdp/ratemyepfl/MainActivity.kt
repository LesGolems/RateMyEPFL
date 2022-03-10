package com.github.sdp.ratemyepfl

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.activities.classrooms.ClassroomsListActivity
import com.github.sdp.ratemyepfl.auth.UserAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    @Inject lateinit var auth: UserAuth

    private lateinit var mLoginButton: Button
    private lateinit var mLogoutButton: Button
    private lateinit var mEmail: TextView

    private lateinit var mRoomReviewButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mEmail = findViewById(R.id.email)
        mLoginButton = findViewById(R.id.loginButton)
        mLogoutButton = findViewById(R.id.logoutButton)
        checkUser()

        mRoomReviewButton = findViewById(R.id.classroomReviewButton)
        mRoomReviewButton.setOnClickListener {
            startActivity(Intent(this, ClassroomsListActivity::class.java))
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