package com.github.sdp.ratemyepfl

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // We get a reference to the view objects, using their previously set ID
        val mNameText = findViewById<EditText>(R.id.mainName)
        val mGoButton = findViewById<Button>(R.id.mainGoButton)
        val mReviewButton = findViewById<Button>(R.id.coursesReviewButton)

        // We then set the behaviour of the button
        // It's quite short, so we can leave it here, but as soon as it starts
        // doing more complex stuff, it should be moved to a separate method
        mGoButton.setOnClickListener {
            val name = mNameText.text.toString()
            sayHello(name)
        }

        mReviewButton.setOnClickListener {
            startReview(mNameText.text.toString())
        }

        // Bonus: trigger the button when the user presses "enter" in the text field
        mNameText.setOnKeyListener{ _, keyCode, event ->
            if((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                mGoButton.performClick()
                true
            }
            false
        }
    }

    private fun sayHello(userName: String) {
        val intent = Intent(this, GreetingActivity::class.java)
        intent.putExtra(GreetingActivity.EXTRA_USER_NAME, userName)
        startActivity(intent)
    }

    private fun startReview(courseName: String) {
        val intent = Intent(this, CourseReviewActivity::class.java)
        intent.putExtra(CourseReviewActivity.EXTRA_COURSE_NAME, courseName)
        startActivity(intent)
    }
}