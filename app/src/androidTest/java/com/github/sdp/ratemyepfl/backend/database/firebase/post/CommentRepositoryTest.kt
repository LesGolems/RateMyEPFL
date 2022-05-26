package com.github.sdp.ratemyepfl.backend.database.firebase.post

import com.github.sdp.ratemyepfl.model.review.Comment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class CommentRepositoryTest {
    private lateinit var currentId: String
    private val testComment = Comment(
        "Fake id",
        "Fake comment"
    )
    private val testComment2 = Comment(
        "Fake id 2",
        "Fake comment"
    )

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var commentRepository: CommentRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
        runTest {
            currentId = commentRepository.add(testComment).await()
        }
    }

    @After
    fun clean() {
        runTest {
            commentRepository.remove(currentId).await()
        }
    }

    @Test
    fun getBySubjectWorks() {
        runTest {
            val comments = commentRepository.getBySubjectId(testComment.subjectId)
            assertNotNull(comments)
            assertEquals(1, comments.size)
            assertEquals("Fake comment", comments.first().comment)
        }
    }

    @Test
    fun likersWorks() {
        runTest {
            commentRepository.addUpVote(currentId, "Fake uid")
            var comment = commentRepository.getById(currentId)
            Assert.assertNotNull(comment!!)
            Assert.assertEquals(1, comment.likers.size)
            Assert.assertEquals("Fake uid", comment.likers[0])

            commentRepository.removeUpVote(currentId, "Fake uid")
            comment = commentRepository.getById(currentId)
            Assert.assertNotNull(comment!!)
            Assert.assertEquals(0, comment.likers.size)
        }
    }

    @Test
    fun dislikersWorks() {
        runTest {
            commentRepository.addDownVote(currentId, "Fake uid")
            var comment = commentRepository.getById(currentId)
            Assert.assertNotNull(comment!!)
            Assert.assertEquals(1, comment.dislikers.size)
            Assert.assertEquals("Fake uid", comment.dislikers[0])

            commentRepository.removeDownVote(currentId, "Fake uid")
            comment = commentRepository.getById(currentId)
            Assert.assertNotNull(comment!!)
            Assert.assertEquals(0, comment.dislikers.size)
        }
    }
}