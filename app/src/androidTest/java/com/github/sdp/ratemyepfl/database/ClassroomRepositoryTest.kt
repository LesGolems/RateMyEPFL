package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepositoryImpl.Companion.toClassroom
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
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
import org.mockito.Mockito.mock
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ClassroomRepositoryTest {
    private val testRoom = Classroom("Fake id", 0.0, 0)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var roomRepo: ClassroomRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
        roomRepo.add(testRoom)
    }

    @After
    fun clean() {
        roomRepo.remove(testRoom.name)
    }

    @Test
    fun conversionTest() = runTest {
        roomRepo.add(testRoom).await()
        val c = roomRepo.getRoomById(testRoom.getId())
        assertEquals(testRoom, c)
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
    fun toItemReturnsAClassroomForCompleteSnapshot() {
        val fake = "fake"

        val snapshot = mock(DocumentSnapshot::class.java)
        Mockito.`when`(snapshot.id).thenReturn(fake)
        Mockito.`when`(snapshot.getString(ClassroomRepositoryImpl.ROOM_NAME_FIELD_NAME))
            .thenReturn(fake)
        Mockito.`when`(snapshot.getString(ClassroomRepositoryImpl.ROOM_KIND_FIELD_NAME))
            .thenReturn(fake)
        Mockito.`when`(snapshot.getField<Int>(ReviewableRepository.NUM_REVIEWS_FIELD_NAME))
            .thenReturn(15)
        Mockito.`when`(snapshot.getDouble(ReviewableRepository.AVERAGE_GRADE_FIELD_NAME))
            .thenReturn(2.5)


        val classroom: Classroom? = snapshot.toClassroom()
        val fakeClassroom = Classroom(fake, 2.5, 15, fake)
        assertEquals(fakeClassroom, classroom)

    }

    @Test
    fun toItemReturnsNullForInCompleteSnapshot() {
        val snapshot = mock(DocumentSnapshot::class.java)

        Mockito.`when`(snapshot.id).thenReturn(null)
        Mockito.`when`(snapshot.getString(ClassroomRepositoryImpl.ROOM_NAME_FIELD_NAME))
            .thenReturn(null)
        Mockito.`when`(snapshot.getString(ClassroomRepositoryImpl.ROOM_KIND_FIELD_NAME))
            .thenReturn(null)
        Mockito.`when`(snapshot.getField<Int>(ReviewableRepository.NUM_REVIEWS_FIELD_NAME))
            .thenReturn(null)
        Mockito.`when`(snapshot.getDouble(ReviewableRepository.AVERAGE_GRADE_FIELD_NAME))
            .thenReturn(null)

        val classroom: Classroom? = snapshot.toClassroom()
        assertEquals(null, classroom)
    }
}