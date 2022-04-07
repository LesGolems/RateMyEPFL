package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.ClassroomRepository.Companion.toClassroom
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.google.firebase.firestore.DocumentSnapshot
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ClassroomRepositoryTest {
    @Test
    fun toItemReturnsAClassroomForCompleteSnapshot() {
        val fake = "fake"
        val fakeCredit = "0"

        val snapshot = mock(DocumentSnapshot::class.java)
        Mockito.`when`(snapshot.id).thenReturn(fake)
        Mockito.`when`(snapshot.getString(ClassroomRepository.ROOM_KIND_FIELD)).thenReturn(fake)
        Mockito.`when`(snapshot.getString(Repository.NUM_REVIEWS_FIELD)).thenReturn("15")
        Mockito.`when`(snapshot.getString(Repository.AVERAGE_GRADE_FIELD)).thenReturn("2.5")

        val classroom: Classroom? = snapshot.toClassroom()
        val fakeClassroom = Classroom(fake, 15, 2.5, fake)
        assertEquals(fakeClassroom, classroom)

    }

    @Test
    fun toItemReturnsNullForInCompleteSnapshot() {
        val fake = "fake"
        val snapshot = Mockito.mock(DocumentSnapshot::class.java)

        Mockito.`when`(snapshot.id).thenReturn(null)
        Mockito.`when`(snapshot.getString(Repository.NUM_REVIEWS_FIELD)).thenReturn(null)
        Mockito.`when`(snapshot.getString(Repository.AVERAGE_GRADE_FIELD)).thenReturn(null)

        val classroom: Classroom? = snapshot.toClassroom()
        assertEquals(null, classroom)
    }
}