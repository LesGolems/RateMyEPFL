package com.github.sdp.ratemyepfl.activity.course

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.CourseReview
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.viewmodel.CourseReviewViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class CourseReviewActivity : AppCompatActivity() {

    companion object {
        const val UNCHECKED_RATING_MESSAGE: String = "Please select a rating"
        const val EMPTY_TITLE_MESSAGE: String = "Please enter a title"
        const val EMPTY_COMMENT_MESSAGE: String = "Please enter a comment"
        const val EXTRA_COURSE_IDENTIFIER: String =
            "com.github.sdp.ratemyepfl.model.review.extra_course_name"
        const val EXTRA_REVIEW: String = "com.github.sdp.ratemyepfl.model.review.extra_review"

        fun onTextChangedTextWatcher(consume: (CharSequence?, Int, Int, Int) -> Unit): TextWatcher =
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    consume(p0, p1, p2, p3)
                }

                override fun afterTextChanged(p0: Editable?) {}
            }
    }

    private lateinit var courseRatingRadioGroup: RadioGroup
    private lateinit var courseReviewTitle: TextInputEditText
    private lateinit var courseReviewComment: TextInputEditText
    private lateinit var lastCourseRatingButton: RadioButton
    private lateinit var submitButton: Button
    private lateinit var courseReviewIndication: TextView
    private lateinit var viewModel: CourseReviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_review)

        val radioGroupLayout = findViewById<RelativeLayout>(R.id.layoutRadioGroupCourseReview)
        courseRatingRadioGroup = radioGroupLayout.findViewById(R.id.courseReviewRating)
        courseReviewTitle = findViewById(R.id.courseReviewTitle)
        courseReviewComment = findViewById(R.id.courseReviewOpinion)
        lastCourseRatingButton =
            radioGroupLayout.findViewById(R.id.courseRatingExcellentRadioButton)
        submitButton = findViewById(R.id.courseReviewSubmit)
        courseReviewIndication = findViewById(R.id.courseReviewCourseName)

        startReview()
    }

    private fun <T> setError(view: TextView, newValue: T?, errorMessage: String) {
        view.error = if (newValue == null) errorMessage else null
    }

    private fun cancelReview() {
        setResult(RESULT_CANCELED, Intent())
        finish()
    }

    private fun startReview() {
        // Set up the view model. If it fails, cancel the review
        try {
            val viewModel by viewModels<CourseReviewViewModel>()
            this.viewModel = viewModel
        } catch (e: IllegalArgumentException) {
            cancelReview()
            return
        }

        courseReviewIndication.text =
            getString(R.string.course_review_indication_string, viewModel.course.toString())

        viewModel.rating.observe(this) { rating ->
            setError(lastCourseRatingButton, rating, UNCHECKED_RATING_MESSAGE)
        }

        viewModel.comment.observe(this) { comment ->
            setError(courseReviewComment, comment, EMPTY_COMMENT_MESSAGE)
        }

        viewModel.title.observe(this) { title ->
            setError(courseReviewTitle, title, EMPTY_TITLE_MESSAGE)
        }

        courseRatingRadioGroup.setOnCheckedChangeListener { _, id ->
            viewModel.setRating(fromIdToRating(id))
        }

        courseReviewTitle.addTextChangedListener(onTextChangedTextWatcher { charSequence, _, _, _ ->
            viewModel.setTitle(charSequence.toString())
        })

        courseReviewComment.addTextChangedListener(onTextChangedTextWatcher { charSequence, _, _, _ ->
            viewModel.setComment(
                charSequence.toString()
            )
        })

        submitButton.setOnClickListener { _ ->
            viewModel.review()?.let { review ->
                submitReview(viewModel.course, review)
            }
        }

    }

    private fun submitReview(course: Course, review: CourseReview) {
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_REVIEW, review.serialize())
        resultIntent.putExtra(
            CourseReviewListActivity.EXTRA_COURSE_JSON,
            Json.encodeToString(course)
        )
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun fromIdToRating(id: Int): ReviewRating? = when (id) {
        R.id.courseRatingTerribleRadioButton -> ReviewRating.TERRIBLE
        R.id.courseRatingPoorRadioButton -> ReviewRating.POOR
        R.id.courseRatingAverageRadioButton -> ReviewRating.AVERAGE
        R.id.courseRatingGoodRadioButton -> ReviewRating.GOOD
        R.id.courseRatingExcellentRadioButton -> ReviewRating.EXCELLENT
        else -> null
    }

}