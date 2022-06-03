package com.github.sdp.ratemyepfl.backend.database.fakes

import com.github.sdp.ratemyepfl.backend.database.post.SubjectRepository
import com.github.sdp.ratemyepfl.model.review.Subject
import com.github.sdp.ratemyepfl.model.review.SubjectKind
import com.github.sdp.ratemyepfl.model.time.DateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeSubjectRepository @Inject constructor() : SubjectRepository, FakeRepository<Subject>() {

    companion object {
        const val FAKE_UID_1 = "ID1"
        const val FAKE_UID_2 = "ID2"
        const val FAKE_UID_3 = "ID3"
        const val FAKE_UID_4 = "ID4"
        val DATE = DateTime(2000, 1, 1, 0, 0)

        val fakeList = listOf(
            Subject.Builder()
                .setKind(SubjectKind.EXAMS)
                .setComments(listOf("c1", "c2"))
                .setTitle("Where is the best place to eat at EPFL?")
                .setComment("I am looking ideally for Asian/Middle-East food for max 20 CHF. Thanks!")
                .setDate(DATE)
                .setUid("AsiDGo8e1QhVmxjQYVTUWIFtBfo1")
                .setLikers(listOf(FAKE_UID_1))
                .setDislikers(listOf(FAKE_UID_2))
                .build(),
            Subject.Builder()
                .setKind(SubjectKind.EXAMS)
                .setComments(listOf("c1", "c2"))
                .setTitle("Does anyone know when should we receive the first results this year?")
                .setComment("")
                .setDate(DATE)
                .setUid("xMhzXCCsyYTfzh7GXEJDR2NvT9G2")
                .setLikers(listOf(FAKE_UID_1))
                .setDislikers(listOf(FAKE_UID_2))
                .build()
        )

        var subjectList = fakeList
    }

    init {
        elements = fakeList.toSet()
    }

    override suspend fun getSubjects(): List<Subject> {
        return subjectList
    }

    override suspend fun addComment(subjectId: String, commentId: String) {
    }

    override suspend fun removeComment(subjectId: String, commentId: String) {
        subjectList = subjectList.map {
            if (it.getId() == subjectId) {
                it.copy(comments = it.comments.minus(commentId))
            } else it
        }
    }

    override fun addWithId(item: Subject, withId: String): Flow<String> {
        elements = elements.plus(item)
        return flowOf(withId)
    }

    override suspend fun addUpVote(postId: String, userId: String) {
        elements.map {
            if (it.getId() == postId) {
                it.copy(likers = it.likers.plus(userId))
            } else it
        }
    }

    override suspend fun removeUpVote(postId: String, userId: String) {
        elements.map {
            if (it.getId() == postId) {
                it.copy(likers = it.likers.minus(userId))
            } else it
        }
    }

    override suspend fun addDownVote(postId: String, userId: String) {
        elements.map {
            if (it.getId() == postId) {
                it.copy(likers = it.dislikers.plus(userId))
            } else it
        }
    }

    override suspend fun removeDownVote(postId: String, userId: String) {
        elements.map {
            if (it.getId() == postId) {
                it.copy(likers = it.dislikers.minus(userId))
            } else it
        }
    }

}