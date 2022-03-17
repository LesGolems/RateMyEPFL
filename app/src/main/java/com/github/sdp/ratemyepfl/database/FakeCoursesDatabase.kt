package com.github.sdp.ratemyepfl.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.CourseReview
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import java.time.LocalDate
import javax.inject.Inject


class FakeCoursesDatabase @Inject constructor() : CourseDatabase {
    private val initialCoursesList = coursesList()
    private val coursesLiveData = MutableLiveData(initialCoursesList)

    private fun coursesList(): List<Course> {
        return listOf(
            Course(
                name="Software development project",
                faculty="IC",
                teacher="George Candea",
                credits=4,
                courseCode="CS-306"
            ),
            Course(
                name="Calcul quantique",
                faculty="IC",
                teacher="Nicolas Macris",
                credits=4,
                courseCode="CS-308"
            ),
            Course(
                name="Intelligence artificielle",
                faculty="IC",
                teacher="Boi Faltings",
                credits=4,
                courseCode="CS-330"
            ),
            Course(
                name="Projet de systems-on-chip",
                faculty="IC",
                teacher="René Beuchat",
                credits=3,
                courseCode="CS-309"
            ),
            Course(
                name="Introduction to database systems",
                faculty="IC",
                teacher="Christoph Koch",
                credits=4,
                courseCode="CS-332"
            )
        )
    }

    override fun getCourses(): LiveData<List<Course>> {
        return coursesLiveData
    }
}