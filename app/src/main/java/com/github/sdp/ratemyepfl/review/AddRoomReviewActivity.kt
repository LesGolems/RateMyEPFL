package com.github.sdp.ratemyepfl.review

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import com.google.android.material.textfield.TextInputEditText

const val ROOM_GRADE = "grade"
const val ROOM_COMMENT = "comment"

class AddRoomReviewActivity : AppCompatActivity() {
    private lateinit var addRoomGrade: TextInputEditText
    private lateinit var addRoomComment: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_room_review_layout)

        findViewById<Button>(R.id.done_button).setOnClickListener {
            addReview()
        }

        addRoomGrade = findViewById(R.id.add_room_grade)
        addRoomComment = findViewById(R.id.add_room_comment)
    }

    /* The onClick action for the done button. Closes the activity and returns the new flower name
    and description as part of the intent. If the name or description are missing, the result is set
    to cancelled. */
    private fun addReview() {
        val resultIntent = Intent()

        if (addRoomGrade.text.isNullOrEmpty() || addRoomComment.text.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
        } else {
            val grade = addRoomGrade.text.toString()
            val comment = addRoomComment.text.toString()
            resultIntent.putExtra(ROOM_GRADE, grade)
            resultIntent.putExtra(ROOM_COMMENT, comment)
            setResult(Activity.RESULT_OK, resultIntent)
        }
        finish()
    }
}