package com.github.sdp.ratemyepfl.backend.database.firebase.reviewable

import com.github.sdp.ratemyepfl.model.items.Classroom
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
}