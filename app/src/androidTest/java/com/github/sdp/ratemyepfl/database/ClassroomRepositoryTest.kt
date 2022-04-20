package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepositoryImpl.Companion.toClassroom
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl
import com.github.sdp.ratemyepfl.model.items.Classroom
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
import org.mockito.Mockito.mock
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ClassroomRepositoryTest {
    private val testRoom = Classroom("Fake id", 0, 0.0)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var roomRepo: ClassroomRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
        roomRepo.addAsync(testRoom)
    }

    @After
    fun clean(){
        roomRepo.removeAsync(testRoom.name)
    }

    @Test
    fun getRoomsWorks() {
        runTest {
            val room = roomRepo.getClassrooms()[0]
            assertEquals(testRoom.name, room.name)
        }
    }

    @Test
    fun getRoomByIdWorks() {
        runTest {
            val room = roomRepo.getRoomById(testRoom.name)
            assertNotNull(room)
            assertEquals(testRoom.name, room!!.name)
        }
    }

    @Test
    fun updateRoomRatingWorks() {
        runTest {
            roomRepo.updateClassroomRating(testRoom.name, ReviewRating.EXCELLENT)
            val room = roomRepo.getRoomById(testRoom.name)
            assertNotNull(room)
            assertEquals(testRoom.name, room!!.name)
            assertEquals(1, room.numReviews)
            assertEquals(5.0, room.averageGrade, 0.1)
        }
    }


    @Test
    fun toItemReturnsAClassroomForCompleteSnapshot() {
        val fake = "fake"

        val snapshot = mock(DocumentSnapshot::class.java)
        Mockito.`when`(snapshot.id).thenReturn(fake)
        Mockito.`when`(snapshot.getString(ClassroomRepositoryImpl.ROOM_KIND_FIELD)).thenReturn(fake)
        Mockito.`when`(snapshot.getString(ReviewableRepositoryImpl.NUM_REVIEWS_FIELD_NAME)).thenReturn("15")
        Mockito.`when`(snapshot.getString(ReviewableRepositoryImpl.AVERAGE_GRADE_FIELD_NAME)).thenReturn("2.5")

        val classroom: Classroom? = snapshot.toClassroom()
        val fakeClassroom = Classroom(fake, 15, 2.5, fake)
        assertEquals(fakeClassroom, classroom)

    }

    @Test
    fun toItemReturnsNullForInCompleteSnapshot() {
        val snapshot = mock(DocumentSnapshot::class.java)

        Mockito.`when`(snapshot.id).thenReturn(null)
        Mockito.`when`(snapshot.getString(ReviewableRepositoryImpl.NUM_REVIEWS_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(ReviewableRepositoryImpl.AVERAGE_GRADE_FIELD_NAME)).thenReturn(null)

        val classroom: Classroom? = snapshot.toClassroom()
        assertEquals(null, classroom)
    }
}