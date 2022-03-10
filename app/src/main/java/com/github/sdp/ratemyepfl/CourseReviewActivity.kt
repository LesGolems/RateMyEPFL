package com.github.sdp.ratemyepfl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.github.sdp.ratemyepfl.review.CourseReview
import com.github.sdp.ratemyepfl.review.ReviewRating
import com.google.android.material.textfield.TextInputEditText

class CourseReviewActivity : AppCompatActivity() {

    companion object {
        const val UNCHECKED_RATING_MESSAGE: String = "Please select a rating"
        const val EMPTY_TITLE_MESSAGE: String = "Please enter a title"
        const val EMPTY_COMMENT_MESSAGE: String = "Please enter a comment"
        const val EXTRA_COURSE_NAME: String = "com.github.sdp.ratemyepfl.review.extra_course_name"
        const val EMPTY_STRING: String = ""
    }

    private lateinit var courseRatingButton: RadioGroup
    private lateinit var courseReviewTitle: TextInputEditText
    private lateinit var courseReviewComment: TextInputEditText
    private lateinit var lastCourseRatingButton: RadioButton
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_review)

        courseRatingButton = findViewById(R.id.courseReviewRating)
        courseReviewTitle = findViewById(R.id.courseReviewTitle)
        courseReviewComment = findViewById(R.id.courseReviewOpinion)
        lastCourseRatingButton = findViewById(R.id.courseRatingTerribleRadioButton)
        submitButton = findViewById(R.id.courseReviewSubmit)

        val courseReviewIndication = findViewById<TextView>(R.id.courseReviewCourseName)
        val courseName: String? = intent.getStringExtra(EXTRA_COURSE_NAME)
        courseReviewIndication.text =
            getString(R.string.course_review_indication_string, courseName)

        submitButton.setOnClickListener { _ ->
            val review = extractReview()
            if (review != null) {
                submitReview(review)
            }
        }
    }


    private fun extractTextField(field: TextInputEditText, errorMessage: String): String? {
        val text: String? = field.text?.toString()
        field.error =
            if (!isTextValid(text))
                errorMessage
            else null
        return text
    }

    private fun isTextValid(text: String?): Boolean = text != null && text != EMPTY_STRING

    private fun extractTitle() = extractTextField(courseReviewTitle, EMPTY_TITLE_MESSAGE)
    private fun extractComment() = extractTextField(courseReviewComment, EMPTY_COMMENT_MESSAGE)

    private fun extractRating(): ReviewRating? {
        val checkedButton = courseRatingButton.checkedRadioButtonId
        val rating = fromIdToRating(checkedButton)
        lastCourseRatingButton.error =
            if (rating == null) UNCHECKED_RATING_MESSAGE else null
        return rating
    }

    private fun extractReview(): CourseReview? {
        val rating = extractRating()
        val title = extractTitle()
        val comment = extractComment()

        if (rating != null && isTextValid(title) && isTextValid(comment)) {
            return CourseReview.Builder()
                .setRate(rating)
                .setTitle(title!!)
                .setComment(comment!!)
                .build()
        }
        return null
    }

    private fun fromIdToRating(id: Int): ReviewRating? = when (id) {
        R.id.courseRatingTerribleRadioButton -> ReviewRating.TERRIBLE
        R.id.courseRatingPoorRadioButton -> ReviewRating.POOR
        R.id.courseRatingAverageRadioButton -> ReviewRating.AVERAGE
        R.id.courseRatingGoodRadioButton -> ReviewRating.GOOD
        R.id.courseRatingExcellentRadioButton -> ReviewRating.EXCELLENT
        else -> null
    }

    private fun submitReview(review: CourseReview) {
        // Currently returns to GreetingActivity, to be changed
        val intent = Intent(this, GreetingActivity::class.java)
        intent.putExtra(GreetingActivity.EXTRA_USER_NAME, review.toString())
        startActivity(intent)
    }
}