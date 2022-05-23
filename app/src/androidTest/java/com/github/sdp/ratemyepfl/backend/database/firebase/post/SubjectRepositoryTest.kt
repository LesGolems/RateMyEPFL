package com.github.sdp.ratemyepfl.backend.database.firebase.post

import com.github.sdp.ratemyepfl.model.review.Subject
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
class SubjectRepositoryTest {
    private lateinit var currentId: String
    private val testSubject = Subject(
        "Fake title",
        "Fake comment"
    )
    private val testSubject2 = Subject(
        "Fake title 2",
        "Fake comment"
    )

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var subjectRepository: SubjectRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
        runTest {
            currentId = subjectRepository.addAndGetId(testSubject)
        }
    }

    @After
    fun clean() {
        runTest {
            subjectRepository.remove(currentId).await()
        }
    }

    @Test
    fun addWithoutIdWorks() {
        runTest {
            subjectRepository.add(testSubject2).await()
            val subjects = subjectRepository.getSubjects()
            assertNotNull(subjects)
            assertEquals(2, subjects.size)
            assertEquals("Fake comment", subjects.last().comment)
            subjectRepository.remove(subjects.last().getId()).await()
        }
    }

    @Test
    fun addAndRemoveCommentWorks(){
        runTest {
            subjectRepository.addComment(currentId, "Fake")
            var subject = subjectRepository.getById(currentId)
            Assert.assertNotNull(subject!!)
            Assert.assertEquals(1, subject.comments.size)
            Assert.assertEquals("Fake", subject.comments.first())

            subjectRepository.removeComment(currentId, "Fake")
            subject = subjectRepository.getById(currentId)
            Assert.assertNotNull(subject!!)
            Assert.assertEquals(0, subject.comments.size)
        }
    }

    @Test
    fun likersWorks() {
        runTest {
            subjectRepository.addUpVote(currentId, "Fake uid")
            var subject = subjectRepository.getById(currentId)
            Assert.assertNotNull(subject!!)
            Assert.assertEquals(1, subject.likers.size)
            Assert.assertEquals("Fake uid", subject.likers[0])

            subjectRepository.removeUpVote(currentId, "Fake uid")
            subject = subjectRepository.getById(currentId)
            Assert.assertNotNull(subject!!)
            Assert.assertEquals(0, subject.likers.size)
        }
    }

    @Test
    fun dislikersWorks() {
        runTest {
            subjectRepository.addDownVote(currentId, "Fake uid")
            var subject = subjectRepository.getById(currentId)
            Assert.assertNotNull(subject!!)
            Assert.assertEquals(1, subject.dislikers.size)
            Assert.assertEquals("Fake uid", subject.dislikers[0])

            subjectRepository.removeDownVote(currentId, "Fake uid")
            subject = subjectRepository.getById(currentId)
            Assert.assertNotNull(subject!!)
            Assert.assertEquals(0, subject.dislikers.size)
        }
    }
}