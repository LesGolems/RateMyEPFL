package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.RestaurantRepository.Companion.toRestaurant
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.Assert.assertEquals
import org.mockito.Mockito
import javax.inject.Inject

@HiltAndroidTest
class RestaurantRepositoryTest /*{
    private val testCourse = Course(
        "title", "section", "teacher",
        7, "Fake id", 0, 0.0)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var courseRepo: CourseRepository

    @Before
    fun setup() {
        hiltRule.inject()
        courseRepo.add(testCourse)
    }

    @After
    fun clean(){
        courseRepo.remove(testCourse.id)
    }

    @Test
    fun getCoursesWorks() {
        runTest {
            val courses = courseRepo.getCourses()
            assertEquals(courses.size, 1)

            val course = courses[0]
            assertEquals(course.id, testCourse.id)
            assertEquals(course.title, testCourse.title)
            assertEquals(course.section, testCourse.section)
            assertEquals(course.teacher, testCourse.teacher)
            assertEquals(course.credits, testCourse.credits)
            assertEquals(course.cycle, testCourse.cycle)
            assertEquals(course.session, testCourse.session)
            assertEquals(course.grading, testCourse.grading)
            assertEquals(course.language, testCourse.language)
            assertEquals(course.numReviews, testCourse.numReviews)
            assertEquals(course.averageGrade, testCourse.averageGrade, 0.1)
        }
    }

    @Test
    fun getCourseByIdWorks() {
        runTest {
            val course = courseRepo.getCourseById(testCourse.id)
            Assert.assertNotNull(course)
            assertEquals(course!!.id, testCourse.id)
            assertEquals(course.title, testCourse.title)
            assertEquals(course.section, testCourse.section)
            assertEquals(course.teacher, testCourse.teacher)
            assertEquals(course.credits, testCourse.credits)
            assertEquals(course.cycle, testCourse.cycle)
            assertEquals(course.session, testCourse.session)
            assertEquals(course.grading, testCourse.grading)
            assertEquals(course.language, testCourse.language)
            assertEquals(course.numReviews, testCourse.numReviews)
            assertEquals(course.averageGrade, testCourse.averageGrade, 0.1)
        }
    }

    @Test
    fun updateCourseRatingWorks() {
        runTest {
            courseRepo.updateCourseRating(testCourse.id, ReviewRating.EXCELLENT).await()
            val course = courseRepo.getCourseById(testCourse.id)
            Assert.assertNotNull(course)
            assertEquals(course!!.id, testCourse.id)
            assertEquals(course.title, testCourse.title)
            assertEquals(course.section, testCourse.section)
            assertEquals(course.teacher, testCourse.teacher)
            assertEquals(course.credits, testCourse.credits)
            assertEquals(course.cycle, testCourse.cycle)
            assertEquals(course.session, testCourse.session)
            assertEquals(course.grading, testCourse.grading)
            assertEquals(course.language, testCourse.language)
            assertEquals(course.numReviews, 1)
            assertEquals(course.averageGrade, 5.0, 0.1)
        }
    }

    @Test
    fun returnsARestaurantForCompleteSnapshot() {
        val fake = "fake"
        val lat = 0.0
        val long = 0.0
        val occupancy = 0

        val snapshot = Mockito.mock(DocumentSnapshot::class.java)
        Mockito.`when`(snapshot.id).thenReturn(fake)
        Mockito.`when`(snapshot.getString(Repository.NUM_REVIEWS_FIELD)).thenReturn("15")
        Mockito.`when`(snapshot.getString(Repository.AVERAGE_GRADE_FIELD)).thenReturn("2.5")
        Mockito.`when`(snapshot.getString("lat")).thenReturn(lat.toString())
        Mockito.`when`(snapshot.getString("long")).thenReturn(long.toString())
        Mockito.`when`(snapshot.getString("occupancy")).thenReturn(occupancy.toString())

        val restaurant = snapshot.toRestaurant()
        val fakeRestaurant = Restaurant.Builder()
            .setId(fake)
            .setNumReviews(15)
            .setAverageGrade(2.5)
            .setLat(lat)
            .setLong(long)
            .build()
        assertEquals(fakeRestaurant, restaurant)
    }

}*/