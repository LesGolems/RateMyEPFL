package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.reviewable.CourseRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepositoryImpl.Companion.toCourse
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class CourseRepositoryImplTest {
    private val testCourse = Course(
        "title", "section", "teacher",
        7, "Fake id", 0, 0.0, "cycle", "session",
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
    fun clean() {
        //courseRepo.remove(testCourse.id)
    }

    @Test
    fun getCoursesWorks() {
        runTest {
            val course = courseRepo.getCourses()[0]

            assertEquals(testCourse.id, course.id)
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
            val course = courseRepo.getCourseById(testCourse.id)
            assertNotNull(course)
            assertEquals(testCourse.id, course!!.id)
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
    fun updateCourseRatingWorks() {
        runTest {
            courseRepo.updateCourseRating(testCourse.id, ReviewRating.EXCELLENT)
            val course = courseRepo.getCourseById(testCourse.id)
            assertNotNull(course)
            assertEquals(testCourse.id, course!!.id)
            assertEquals(1, course.numReviews)
            assertEquals(5.0, course.averageGrade, 0.1)
        }
    }

    @Test
    fun toItemReturnsACourseForCompleteSnapshot() {
        val fake = "fake"
        val fakeCredit = "0"

        val snapshot = Mockito.mock(DocumentSnapshot::class.java)
        Mockito.`when`(snapshot.id).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepositoryImpl.TITLE_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepositoryImpl.SECTION_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepositoryImpl.TEACHER_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepositoryImpl.CREDITS_FIELD_NAME))
            .thenReturn(fakeCredit)
        Mockito.`when`(snapshot.getString(ReviewableRepositoryImpl.NUM_REVIEWS_FIELD_NAME)).thenReturn("15")
        Mockito.`when`(snapshot.getString(ReviewableRepositoryImpl.AVERAGE_GRADE_FIELD_NAME)).thenReturn("2.5")

        val course: Course? = snapshot.toCourse()
        val fakeCourse = Course(fake, fake, fake, fakeCredit.toInt(), fake, 15, 2.5)
        assertEquals(fakeCourse, course)

    }

    @Test
    fun toItemReturnsNullForInCompleteSnapshot() {
        val fake = "fake"
        val snapshot = Mockito.mock(DocumentSnapshot::class.java)

        Mockito.`when`(snapshot.id).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepositoryImpl.TITLE_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(CourseRepositoryImpl.SECTION_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(CourseRepositoryImpl.TEACHER_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(CourseRepositoryImpl.CREDITS_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(ReviewableRepositoryImpl.NUM_REVIEWS_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(ReviewableRepositoryImpl.AVERAGE_GRADE_FIELD_NAME)).thenReturn(null)

        val course: Course? = snapshot.toCourse()
        assertEquals(null, course)
    }
}