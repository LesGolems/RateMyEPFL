package com.github.sdp.ratemyepfl.activity.classrooms

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.course.CourseReviewActivity
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.viewmodel.AddRoomReviewViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

const val ROOM_GRADE = "grade"
const val ROOM_COMMENT = "comment"

@AndroidEntryPoint
class AddRoomReviewActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ROOM_REVIEWED: String =
            "com.github.sdp.activity.classrooms.extra_room_reviewed"

        const val NO_GRADE_MESSAGE: String = "You need to give a grade !"
    }

    private lateinit var ratingBar: RatingBar
    private lateinit var addRoomComment: TextInputEditText
    private lateinit var reviewIndicationTitle: TextView
    private lateinit var scoreTextView: TextView
    private var roomReviewed: String? = null

    val viewModel: AddRoomReviewViewModel by viewModels<AddRoomReviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_room_review_layout)

        roomReviewed = intent.getStringExtra(EXTRA_ROOM_REVIEWED)
        viewModel.setRoomName(roomReviewed)

        findViewById<Button>(R.id.done_button).setOnClickListener {
            addReview()
        }

        ratingBar = findViewById(R.id.roomReviewRatingBar)
        addRoomComment = findViewById(R.id.add_room_comment)
        reviewIndicationTitle = findViewById(R.id.room_review_title)
        scoreTextView = findViewById(R.id.overallScoreTextView)

        viewModel.rating.observe(this) { rating ->
            scoreTextView.text =
                getString(
                    R.string.overall_score_classroom_review,
                    rating?.toString() ?: NO_GRADE_MESSAGE
                )
        }

        ratingBar.setOnRatingBarChangeListener { _, float, _ ->
            val rating = ReviewRating.fromValue(float)
            viewModel.setRating(rating)
        }

        addRoomComment.addTextChangedListener(CourseReviewActivity.onTextChangedTextWatcher { text, _, _, _ ->
            viewModel.setComment(text?.toString())
        })

        reviewIndicationTitle.text = getString(R.string.title_review_classroom, roomReviewed)
    }

    /* The onClick action for the done button. Closes the activity and returns the room review grade
    and comment as part of the intent. If the grade or comment are missing, the result is set
    to cancelled. */
    private fun addReview() {
        val resultIntent = Intent()
        val rating = viewModel.getRating()
        val comment = viewModel.getComment()

        if (rating == null || comment == null) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
        } else {
            viewModel.submitReview()
            resultIntent.putExtra(ROOM_GRADE, rating.rating)
            resultIntent.putExtra(ROOM_COMMENT, comment)
            setResult(Activity.RESULT_OK, resultIntent)
        }
        finish()
    }

}