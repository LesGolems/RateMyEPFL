package com.github.sdp.ratemyepfl.backend.database.firebase.reviewable

import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.CourseRepositoryImpl.Companion.toCourse
import com.github.sdp.ratemyepfl.backend.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Course
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class CourseRepositoryTest {
    private val testCourse = Course(
        "title", "section", "teacher",
        7, "Fake id", 0.0, 0, "cycle", "session",
        "grading", "language"
    )

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var courseRepo: CourseRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
        courseRepo.add(testCourse)
    }

    @After
    fun clean() = runTest {
        courseRepo.remove(testCourse.getId()).await()
    }

    @Test
    fun getCoursesWorks() {
        runTest {
            val course = courseRepo.getCourses()[0]

            assertEquals(testCourse.courseCode, course.courseCode)
            assertEquals(testCourse.title, course.title)
            assertEquals(testCourse.section, course.section)
            assertEquals(testCourse.teacher, course.teacher)
            assertEquals(testCourse.credits, course.credits)
            assertEquals(testCourse.cycle, course.cycle)
            assertEquals(testCourse.session, course.session)
            assertEquals(testCourse.grading, course.grading)
            assertEquals(testCourse.language, course.language)
        }
    }

    @Test
    fun getCourseByIdWorks() {
        runTest {
            val course = courseRepo.getCourseById(testCourse.courseCode)
            assertNotNull(course)
            assertEquals(testCourse.courseCode, course!!.courseCode)
            assertEquals(testCourse.title, course.title)
            assertEquals(testCourse.section, course.section)
            assertEquals(testCourse.teacher, course.teacher)
            assertEquals(testCourse.credits, course.credits)
            assertEquals(testCourse.cycle, course.cycle)
            assertEquals(testCourse.grade, course.grade, 0.01)
            assertEquals(testCourse.numReviews, course.numReviews)
            assertEquals(testCourse.session, course.session)
            assertEquals(testCourse.grading, course.grading)
            assertEquals(testCourse.language, course.language)
        }
    }

}