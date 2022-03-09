package com.github.sdp.ratemyepfl

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.items.Course
import com.github.sdp.ratemyepfl.review.CourseReview
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/*
Mock class, to be replaced when Nicolas is done
 */
class CourseMgtActivity : AppCompatActivity() {

    private lateinit var reviewButton: Button

    companion object {
        const val EXTRA_COURSE_REVIEWED = "com.github.sdp.ratemyepfl.review.extra_course_reviewed"
        val DEFAULT_COURSE: Course = Course("Sweng", "CS", "Candea", 4, "CS-306")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_management)

        reviewButton = findViewById(R.id.startCourseReviewButton)

        reviewButton.setOnClickListener {
            startReview(DEFAULT_COURSE)
        }
    }


    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val serializedReview = data?.getStringExtra(CourseReviewActivity.EXTRA_REVIEW)
                val courseCode = data?.getStringExtra(EXTRA_COURSE_REVIEWED)
                if (serializedReview != null && courseCode != null) {
                    // Only examples to retrieve the data
                    val review = CourseReview.deserialize(serializedReview)
                    //val course = retrieveCourseFromCourseCode(courseCode)
                }
            }
        }

    private fun retrieveCourseFromCourseCode(code: String): Course {
        return DEFAULT_COURSE
    }

    private fun processResult(course: Course, review: CourseReview) {
        // Do some operations
    }

    private fun startReview(course: Course) {

        val intent = Intent(this, CourseReviewActivity::class.java)
        intent.putExtra(CourseReviewActivity.EXTRA_COURSE_IDENTIFIER, Json.encodeToString(course))
        resultLauncher.launch(intent)

    }
}