package com.github.sdp.ratemyepfl.database

import androidx.test.platform.app.InstrumentationRegistry
import com.github.sdp.ratemyepfl.database.ClassroomRepository.Companion.toClassroom
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

@HiltAndroidTest
class ClassroomRepositoryTest {

    companion object {
        @BeforeClass
        fun setUp() {
            val firestore = Firebase.firestore
            firestore.useEmulator("10.0.2.2", 8080)
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build()
            firestore.firestoreSettings = settings
            roomRepo = ClassroomRepository(firestore)
        }

        private lateinit var roomRepo: ClassroomRepository
    }

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