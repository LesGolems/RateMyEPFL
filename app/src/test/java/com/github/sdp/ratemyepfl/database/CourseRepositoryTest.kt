package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.CourseRepository.Companion.toCourse
import com.github.sdp.ratemyepfl.model.items.Course
import com.google.firebase.firestore.DocumentSnapshot
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class CourseRepositoryTest {
    fun toItemReturnsACourseForCompleteSnapshot() {
        val fake = "fake"
        val fakeCredit = "0"

        val snapshot = Mockito.mock(DocumentSnapshot::class.java)
        Mockito.`when`(snapshot.id).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepository.TITLE_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepository.SECTION_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepository.TEACHER_FIELD_NAME)).thenReturn(fake)
        Mockito.`when`(snapshot.getString(CourseRepository.CREDITS_FIELD_NAME)).thenReturn(fakeCredit)

        val course: Course? = snapshot.toCourse()
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

        val course: Course? = snapshot.toCourse()
        assertEquals(null, course)
    }
}