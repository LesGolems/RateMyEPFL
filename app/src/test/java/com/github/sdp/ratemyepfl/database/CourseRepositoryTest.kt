package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Course
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CourseRepositoryTest2 {
    @Test
    fun toItemReturnsACourseForCompleteSnapshot() {
        val fake = "fake"
        val fakeCredit = "0"
        val firestore = Mockito.mock(FirebaseFirestore::class.java)
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


}