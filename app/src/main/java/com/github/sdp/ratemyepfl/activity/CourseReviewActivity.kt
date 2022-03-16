package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.CourseReview
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.placeholder.ReviewsRepository
import com.github.sdp.ratemyepfl.viewmodel.CourseReviewViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CourseReviewActivity : AppCompatActivity() {

    companion object {
        const val UNCHECKED_RATING_MESSAGE: String = "Please select a rating"
        const val EMPTY_TITLE_MESSAGE: String = "Please enter a title"
        const val EMPTY_COMMENT_MESSAGE: String = "Please enter a comment"
        const val EXTRA_COURSE_IDENTIFIER: String =
            "com.github.sdp.ratemyepfl.model.review.extra_course_name"
        const val EXTRA_REVIEW: String = "com.github.sdp.ratemyepfl.model.review.extra_review"
    }

    private lateinit var courseRatingButton: RadioGroup
    private lateinit var courseReviewTitle: TextInputEditText
    private lateinit var courseReviewComment: TextInputEditText
    private lateinit var lastCourseRatingButton: RadioButton
    private lateinit var submitButton: Button
    private lateinit var courseReviewIndication: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_review)

        courseRatingButton = findViewById(R.id.courseReviewRating)
        courseReviewTitle = findViewById(R.id.courseReviewTitle)
        courseReviewComment = findViewById(R.id.courseReviewOpinion)
        lastCourseRatingButton = findViewById(R.id.courseRatingExcellentRadioButton)
        submitButton = findViewById(R.id.courseReviewSubmit)
        courseReviewIndication = findViewById(R.id.courseReviewCourseName)

        startReview()
    }

    private fun <T> setError(view: TextView, newValue: T?, errorMessage: String) {
        view.error = if (newValue == null) errorMessage else null
    }

    private fun startReview() {
        val serializedCourse: String? = intent.getStringExtra(EXTRA_COURSE_IDENTIFIER)
        if (serializedCourse == null) {
            setResult(RESULT_CANCELED, Intent())
            finish()
        } else {

            val course: Course = Json.decodeFromString(serializedCourse!!)
            courseReviewIndication.text =
                getString(R.string.course_review_indication_string, course.toString())
            val viewModel =
                ViewModelProvider(this, CourseReviewViewModel.CourseReviewViewModelFactory(course))
                    .get(CourseReviewViewModel::class.java)

            viewModel.rating.observe(this) { rating ->
                setError(lastCourseRatingButton, rating, UNCHECKED_RATING_MESSAGE)
            }
            viewModel.comment.observe(this) { comment ->
                setError(courseReviewComment, comment, EMPTY_COMMENT_MESSAGE)
            }
            viewModel.title.observe(this) { title ->
                setError(courseReviewTitle, title, EMPTY_TITLE_MESSAGE)
            }

            courseRatingButton.setOnCheckedChangeListener { _, id ->
                viewModel.setRating(fromIdToRating(id))
            }

            courseReviewTitle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel.setTitle(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            courseReviewComment.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel.setComment(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

            submitButton.setOnClickListener { _ ->
                viewModel.review()?.let { review ->
                    submitReview(viewModel.course, review)
                }
            }
        }
    }

    private fun submitReview(course: Course, review: CourseReview) {
        val repo = ReviewsRepository()
        runBlocking {
            launch {
                repo.add(review)
            }
        }
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_REVIEW, review.serialize())
        resultIntent.putExtra(
            CourseMgtActivity.EXTRA_COURSE_REVIEWED,
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