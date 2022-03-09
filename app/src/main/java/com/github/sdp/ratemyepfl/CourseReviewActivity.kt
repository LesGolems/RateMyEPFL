package com.github.sdp.ratemyepfl

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.items.Course
import com.github.sdp.ratemyepfl.review.CourseReview
import com.github.sdp.ratemyepfl.review.ReviewRating
import com.google.android.material.textfield.TextInputEditText
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.util.*

class CourseReviewActivity : AppCompatActivity() {

    companion object {
        const val UNCHECKED_RATING_MESSAGE: String = "Please select a rating"
        const val EMPTY_TITLE_MESSAGE: String = "Please enter a title"
        const val EMPTY_COMMENT_MESSAGE: String = "Please enter a comment"
        const val EXTRA_COURSE_IDENTIFIER: String =
            "com.github.sdp.ratemyepfl.review.extra_course_name"
        const val EMPTY_STRING: String = ""
        const val EXTRA_REVIEW: String = "com.github.sdp.ratemyepfl.review.extra_review"
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
        lastCourseRatingButton = findViewById(R.id.courseRatingExcellentRadioButton)
        submitButton = findViewById(R.id.courseReviewSubmit)

        val courseReviewIndication = findViewById<TextView>(R.id.courseReviewCourseName)
        val serializedCourse: String? = intent.getStringExtra(EXTRA_COURSE_IDENTIFIER)
        val course: Optional<Course> =
            if (serializedCourse != null) Optional.of(Json.decodeFromString<Course>(serializedCourse))
            else Optional.empty()

        courseReviewIndication.text =
            getString(R.string.course_review_indication_string, course.map { c ->
                String.format("%s %s", c.courseCode, c.name)
            }.orElse(null))

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
                .setDate(LocalDate.now())
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
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_REVIEW, CourseReview.serialize(review))
        resultIntent.putExtra(
            CourseMgtActivity.EXTRA_COURSE_REVIEWED,
            intent.getStringExtra(EXTRA_COURSE_IDENTIFIER)
        )
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}