package com.github.sdp.ratemyepfl.backend.database.firebase.post

import com.github.sdp.ratemyepfl.model.review.Comment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
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
class CommentRepositoryTest {
    private lateinit var currentId: String
    private val testComment = Comment(
        "",
        "Fake id",
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
            currentId = commentRepository.add(testComment).last()
        }
    }

    @After
    fun clean() {
        runTest {
            commentRepository.remove(currentId).collect()
        }
    }

    @Test
    fun getBySubjectWorks() {
        runTest {
            val comments = commentRepository.getBySubjectId(testComment.subjectId).last()
            assertNotNull(comments)
            assertEquals(1, comments.size)
            assertEquals("Fake comment", comments.first().comment)
        }
    }

    @Test
    fun likersWorks() {
        runTest {
            commentRepository.addUpVote(currentId, "Fake uid")
            var comment = commentRepository.getById(currentId).last()
            assertEquals(1, comment.likers.size)
            assertEquals("Fake uid", comment.likers[0])

            commentRepository.removeUpVote(currentId, "Fake uid")
            comment = commentRepository.getById(currentId).last()
            assertEquals(0, comment.likers.size)
        }
    }

    @Test
    fun dislikersWorks() {
        runTest {
            commentRepository.addDownVote(currentId, "Fake uid")
            var comment = commentRepository.getById(currentId).last()
            assertNotNull(comment)
            assertEquals(1, comment.dislikers.size)
            assertEquals("Fake uid", comment.dislikers[0])

            commentRepository.removeDownVote(currentId, "Fake uid")
            comment = commentRepository.getById(currentId).last()
            assertNotNull(comment)
            assertEquals(0, comment.dislikers.size)
        }
    }
}