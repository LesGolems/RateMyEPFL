package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.classrooms.ClassroomsListActivity
import com.github.sdp.ratemyepfl.activity.course.CourseListActivity
import com.github.sdp.ratemyepfl.activity.restaurants.RestaurantListActivity
import com.github.sdp.ratemyepfl.auth.Authenticator
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var user: ConnectedUser
    @Inject
    lateinit var auth: Authenticator

    private lateinit var mLogoutButton: Button
    private lateinit var mCoursesButton: Button
    private lateinit var mUser_text: TextView
    private lateinit var mRoomReviewButton: Button
    private lateinit var mRestaurantButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mUser_text = findViewById(R.id.userText)
        mLogoutButton = findViewById(R.id.logoutButton)
        checkUser()

        mCoursesButton = findViewById(R.id.coursesButton)
        mRestaurantButton = findViewById(R.id.restaurantButton)

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
        val intent = Intent(this, CourseListActivity::class.java)
        startActivity(intent)
    }

    private fun setUpButtons() {
        mLogoutButton.setOnClickListener {
            auth.signOut(applicationContext).addOnCompleteListener {
                startActivity(Intent(this, SplashScreen::class.java))
            }
        }

        mCoursesButton.setOnClickListener {
            displayCourses()
        }

        mRestaurantButton.setOnClickListener {
            startActivity(Intent(this, RestaurantListActivity::class.java))
        }

    }

    private fun checkUser() {
        if (!user.isLoggedIn()) {
            mLogoutButton.isEnabled = false
            mUser_text.text = getString(R.string.visitor)
        } else {
            mLogoutButton.isEnabled = true
            mUser_text.text = user.getEmail()
        }
    }
}

