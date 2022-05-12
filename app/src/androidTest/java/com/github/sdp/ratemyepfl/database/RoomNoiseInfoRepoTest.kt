package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.RoomNoiseInfo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class RoomNoiseInfoRepoTest {
    private val testDate = LocalDateTime.of(2022, 55, 10, 18, 45, 18)
    private val testDateString = testDate.toString()

    private val testRoomNoiseInfo = RoomNoiseInfo("id", mapOf(Pair(testDateString, 38)))

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var roomNoiseRepo: RoomNoiseRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
        runTest {
            roomNoiseRepo.add(testRoomNoiseInfo).await()
        }
    }

    @Test
    fun addMeasurementWhenIdExists() {
        runTest {
            roomNoiseRepo.addMeasurement(testRoomNoiseInfo.roomId, testDate, 50).await()
            val roomNoiseInfo = roomNoiseRepo.getRoomNoiseInfoById(testRoomNoiseInfo.roomId)
            assertNotNull(roomNoiseInfo)
            assertNotNull(roomNoiseInfo!!.noiseData[testDateString])
            assertEquals(50, roomNoiseInfo.noiseData[testDateString])
        }
    }

    @Test
    fun addMeasurementWhenIdNotExists() {
        runTest {
            roomNoiseRepo.addMeasurement("new id", testDate, 50).await()
            val roomNoiseInfo = roomNoiseRepo.getRoomNoiseInfoById("new id")
            assertNotNull(roomNoiseInfo)
            assertNotNull(roomNoiseInfo!!.noiseData[testDateString])
            assertEquals(50, roomNoiseInfo.noiseData[testDateString])
        }
    }
}