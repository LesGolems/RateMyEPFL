package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.ClassroomRepository.Companion.toClassroom
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import javax.inject.Inject

@HiltAndroidTest
class ClassroomRepositoryTest {
    private val testRoom = Classroom("Fake id", 0, 0.0)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var roomRepo: ClassroomRepository

    @Before
    fun setup() {
        hiltRule.inject()
        roomRepo.add(testRoom)
    }

    @After
    fun clean(){
        roomRepo.remove(testRoom.id)
    }

    @Test
    fun getRoomsWorks() {
        runTest {
            val rooms = roomRepo.getClassrooms()
            assertEquals(rooms.size, 1)

            val room = rooms[0]
            assertEquals(room.id, testRoom.id)
            assertEquals(room.numReviews, testRoom.numReviews)
            assertEquals(room.averageGrade, testRoom.averageGrade, 0.1)
        }
    }

    @Test
    fun getRoomByIdWorks() {
        runTest {
            val room = roomRepo.getRoomById(testRoom.id)
            assertNotNull(room)
            assertEquals(room!!.id, testRoom.id)
            assertEquals(room.numReviews, testRoom.numReviews)
            assertEquals(room.averageGrade, testRoom.averageGrade, 0.1)
        }
    }

    @Test
    fun updateRoomRatingWorks() {
        runTest {
            roomRepo.updateClassroomRating(testRoom.id, ReviewRating.EXCELLENT).await()
            val room = roomRepo.getRoomById(testRoom.id)
            assertNotNull(room)
            assertEquals(room!!.id, testRoom.id)
            assertEquals(room.numReviews, 1)
            assertEquals(room.averageGrade, 5.0, 0.1)
        }
    }

    @Test
    fun toItemReturnsAClassroomForCompleteSnapshot() {
        val fake = "fake"

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
        val snapshot = mock(DocumentSnapshot::class.java)

        Mockito.`when`(snapshot.id).thenReturn(null)
        Mockito.`when`(snapshot.getString(Repository.NUM_REVIEWS_FIELD)).thenReturn(null)
        Mockito.`when`(snapshot.getString(Repository.AVERAGE_GRADE_FIELD)).thenReturn(null)

        val classroom: Classroom? = snapshot.toClassroom()
        assertEquals(null, classroom)
    }
}