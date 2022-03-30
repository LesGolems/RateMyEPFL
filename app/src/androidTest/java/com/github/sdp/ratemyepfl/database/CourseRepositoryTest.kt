package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Course
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class CourseRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var repository: ItemRepository<Course>

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getItemsReturnsCorrectItems() = runTest {
        this.launch {
            repository.getItems()
                .zip(FakeCourseRepository.COURSE_LIST)
                .forEach { (i1, i2) ->
                    assertEquals(i2, i1)
                }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getItemByIdReturnsCorrectItem() = runTest {
        this.launch {
            assertEquals(FakeCourseRepository.COURSE_BY_ID, repository.getItemById("fake id"))
        }
    }

    @Test
    fun toItemReturnsACourseForCompleteSnapshot() {
        val fake = "fake"
        val fakeCredit = "0"
        val snapshot = Mockito.mock(DocumentSnapshot::class.java)

        Mockito.`when`(snapshot.id).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepository.TITLE_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepository.SECTION_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepository.TEACHER_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepository.CREDITS_FIELD_NAME)).thenReturn(fakeCredit)

        val courseRepository = CourseRepository()
        val course: Course? = courseRepository.toItem(snapshot)
        val fakeCourse = Course(fake, fake, fake, fakeCredit.toInt(), fake)
        assertEquals(fakeCourse, course)

    }

    @Test
    fun toItemReturnsNullForInCompleteSnapshot() {
        val fake = "fake"
        val fakeCredit = "0"
        val snapshot = Mockito.mock(DocumentSnapshot::class.java)

        Mockito.`when`(snapshot.id).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepository.TITLE_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(CourseRepository.SECTION_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(CourseRepository.TEACHER_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(CourseRepository.CREDITS_FIELD_NAME)).thenReturn(null)

        val courseRepository = CourseRepository()
        val course: Course? = courseRepository.toItem(snapshot)
        assertEquals(null, course)
    }

}