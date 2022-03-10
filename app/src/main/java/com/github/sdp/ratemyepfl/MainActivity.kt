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
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var auth: UserAuth

    private lateinit var mLoginButton: Button
    private lateinit var mLogoutButton: Button
    private lateinit var mCoursesButton: Button
    private lateinit var mReviewButton: Button
    private lateinit var mEmail: TextView

    private lateinit var mRoomReviewButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mEmail = findViewById(R.id.email)
        mLoginButton = findViewById(R.id.loginButton)
        mLogoutButton = findViewById(R.id.logoutButton)
        checkUser()

        mCoursesButton = findViewById<Button>(R.id.coursesButton)
        mReviewButton = findViewById<Button>(R.id.coursesReviewButton)

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

    private fun displayCourses() {
        val intent = Intent(this, CoursesActivity::class.java)
        startActivity(intent)
    }

    private fun startReview() {
        val intent = Intent(this, CourseMgtActivity::class.java)
        startActivity(intent)
    }

    private fun setUpButtons() {
        mLoginButton.setOnClickListener {
            auth.signIn(this)
            checkUser()
        }

        mLogoutButton.setOnClickListener {
            auth.signOut(applicationContext).addOnCompleteListener {
                checkUser()
            }
        }

        mCoursesButton.setOnClickListener {
            displayCourses()
        }

        mReviewButton.setOnClickListener {
            // To be changed once courses are implemented
            startReview()
        }
    }

    private fun checkUser() {
        if (!auth.isLoggedIn()) {
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

