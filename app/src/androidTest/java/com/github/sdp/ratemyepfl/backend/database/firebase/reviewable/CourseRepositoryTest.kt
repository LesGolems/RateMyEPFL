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
    fun conversionTest() = runTest {
        courseRepo.add(testCourse).await()
        val c = courseRepo.getCourseById(testCourse.getId())
        assertEquals(testCourse, c)
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

    @Test
    fun toItemReturnsACourseForCompleteSnapshot() {
        val fake = "fake"
        val fakeCredit = 0

        val snapshot = Mockito.mock(DocumentSnapshot::class.java)
        Mockito.`when`(snapshot.id).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepositoryImpl.TITLE_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepositoryImpl.SECTION_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepositoryImpl.TEACHER_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getField<Int>(CourseRepositoryImpl.CREDITS_FIELD_NAME))
            .thenReturn(fakeCredit)
        Mockito.`when`(snapshot.getString(CourseRepositoryImpl.COURSE_CODE_FIELD_NAME))
            .thenReturn(fake)
        Mockito.`when`(snapshot.getField<Int>(ReviewableRepository.NUM_REVIEWS_FIELD_NAME))
            .thenReturn(15)
        Mockito.`when`(snapshot.getDouble(ReviewableRepository.AVERAGE_GRADE_FIELD_NAME))
            .thenReturn(2.5)

        val course: Course? = snapshot.toCourse()
        val fakeCourse = Course(fake, fake, fake, fakeCredit, fake, 2.5, 15)
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
        Mockito.`when`(snapshot.getString(CourseRepositoryImpl.COURSE_CODE_FIELD_NAME))
            .thenReturn(null)
        Mockito.`when`(snapshot.getField<Int>(ReviewableRepository.NUM_REVIEWS_FIELD_NAME))
            .thenReturn(null)
        Mockito.`when`(snapshot.getDouble(ReviewableRepository.AVERAGE_GRADE_FIELD_NAME))
            .thenReturn(null)

        val course: Course? = snapshot.toCourse()
        assertEquals(null, course)
    }
}