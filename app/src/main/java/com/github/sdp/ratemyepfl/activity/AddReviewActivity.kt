package com.github.sdp.ratemyepfl.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.viewmodel.AddReviewViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

const val ROOM_GRADE = "grade"
const val ROOM_COMMENT = "comment"

@AndroidEntryPoint
class AddReviewActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ITEM_REVIEWED: String =
            "com.github.sdp.activity.classrooms.extra_item_reviewed"

        const val NO_GRADE_MESSAGE: String = "You need to give a grade !"

        fun onTextChangedTextWatcher(consume: (CharSequence?, Int, Int, Int) -> Unit): TextWatcher =
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    consume(p0, p1, p2, p3)
                }

                override fun afterTextChanged(p0: Editable?) {}
            }
    }

    private lateinit var ratingBar: RatingBar
    private lateinit var comment: TextInputEditText
    private lateinit var title: TextInputEditText
    private lateinit var reviewIndicationTitle: TextView
    private lateinit var scoreTextView: TextView
    private var reviewableId: String? = null

    val viewModel: AddReviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_review_layout)

        reviewableId = intent.getStringExtra(EXTRA_ITEM_REVIEWED)

        findViewById<Button>(R.id.doneButton).setOnClickListener {
            addReview()
        }

        ratingBar = findViewById(R.id.reviewRatingBar)
        comment = findViewById(R.id.addReviewComment)
        title = findViewById(R.id.addReviewTitle)
        reviewIndicationTitle = findViewById(R.id.reviewTitle)
        scoreTextView = findViewById(R.id.overallScoreTextView)


        viewModel.rating.observe(this) { rating ->
            scoreTextView.text =
                getString(
                    R.string.overall_score_review,
                    rating?.toString() ?: NO_GRADE_MESSAGE
                )
        }

        ratingBar.setOnRatingBarChangeListener { _, float, _ ->
            val rating = ReviewRating.fromValue(float)
            viewModel.setRating(rating)
        }

        comment.addTextChangedListener(onTextChangedTextWatcher { text, _, _, _ ->
            viewModel.setComment(text?.toString())
        })

        title.addTextChangedListener(onTextChangedTextWatcher { text, _, _, _ ->
            viewModel.setTitle(text?.toString())
        })

        reviewIndicationTitle.text = getString(R.string.title_review, reviewableId)
    }

    /* The onClick action for the done button. Closes the activity and returns the room review grade
    and comment as part of the intent. If the grade or comment are missing, the result is set
    to cancelled. */
    private fun addReview() {
        val resultIntent = Intent()

        if (!viewModel.submitReview(reviewableId)) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
        } else {
            setResult(Activity.RESULT_OK, resultIntent)
        }
        finish()
    }

}