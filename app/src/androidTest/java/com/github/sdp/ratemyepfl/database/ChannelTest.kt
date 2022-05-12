package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.messaging.Channel
import com.github.sdp.ratemyepfl.model.messaging.Message
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class ChannelTest {

    private val message1 = Message(
        senderId = "0000",
        content = "hello world"
    )

    private val message2 = Message(
        senderId = "1111",
        content = "how are you?"
    )

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var channel: Channel

    @Before
    fun setUp() = runTest {
        hiltRule.inject()
        channel = Channel(
            id = "1234",
            title = "fake title",
            members = listOf("0000", "1111"),
            database =  db
        )
    }

    @After
    fun clean() = runTest {
        db.collection("channels")
            .document(channel.id)
            .delete()
            .await()
    }

    @Test
    fun postWorks() {
        runTest {
            assertNull(channel.lastMessage)
            channel.post(message1)
            assertNotNull(channel.lastMessage)
            assertEquals(channel.lastMessage!!.content, message1.content)
            channel.post(message2)
            assertNotNull(channel.lastMessage)
            assertEquals(channel.lastMessage!!.content, message2.content)
        }
    }


    @Test
    fun startListeningWorks() {
        runTest {
            assertNull(channel.lastMessage)

            channel.startListening {
                assertThat(it.map { m -> m.content }, hasItem(message1.content))
            }

            db.collection("channels")
                .document(channel.id)
                .collection("thread")
                .add(message1)
                .await()

            channel.stopListening()

            assertNotNull(channel.lastMessage)
        }
    }

    @Test
    fun stopListeningWorks() {
        runTest {
            assertNull(channel.lastMessage)

            channel.startListening {
                assertThat(it.map { m -> m.content }, not(hasItem(message1.content)))
            }

            channel.stopListening()

            db.collection("channels")
                .document(channel.id)
                .collection("thread")
                .add(message1)
                .await()

            assertNotNull(channel.lastMessage)
        }
    }
}