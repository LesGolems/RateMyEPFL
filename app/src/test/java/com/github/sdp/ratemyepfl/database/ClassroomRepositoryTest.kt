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

        val classroom: Classroom? = snapshot.toClassroom()
        val fakeClassroom = Classroom(fake, fake)
        assertEquals(fakeClassroom, classroom)

    }

    @Test
    fun toItemReturnsNullForInCompleteSnapshot() {
        val fake = "fake"
        val snapshot = Mockito.mock(DocumentSnapshot::class.java)

        Mockito.`when`(snapshot.id).thenReturn(null)

        val classroom: Classroom? = snapshot.toClassroom()
        assertEquals(null, classroom)
    }
}