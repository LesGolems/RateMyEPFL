package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.post.SubjectRepository
import com.github.sdp.ratemyepfl.database.post.SubjectRepositoryImpl.Companion.toSubject
import com.github.sdp.ratemyepfl.database.query.Query
import com.github.sdp.ratemyepfl.model.review.Subject
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction
import org.mockito.Mockito
import java.time.LocalDate
import javax.inject.Inject

class FakeSubjectRepository @Inject constructor() : SubjectRepository {

    companion object {
        const val FAKE_UID_1 = "ID1"
        const val FAKE_UID_2 = "ID2"
        const val FAKE_UID_3 = "ID3"
        const val FAKE_UID_4 = "ID4"

        val fakeList = listOf(
            Subject(
                "Where is the best place to eat at EPFL?",
                "I am looking ideally for Asian/Middle-East food for max 20 CHF. Thanks!",
                LocalDate.now(),
                "AsiDGo8e1QhVmxjQYVTUWIFtBfo1",
                likers = listOf(FAKE_UID_1),
                dislikers = listOf(FAKE_UID_2),
                commentators = listOf(FAKE_UID_1, FAKE_UID_2)
            ),
            Subject(
                "What is the drun is the best place to eat at EPFL?",
                "I am looking ideally for Asian/Middle-East food for max 20 CHF. Thanks!",
                LocalDate.now(),
                "xMhzXCCsyYTfzh7GXEJDR2NvT9G2",
                likers = listOf(FAKE_UID_1),
                dislikers = listOf(FAKE_UID_2),
                commentators = listOf(FAKE_UID_1, FAKE_UID_2)
            )
        )

        var subjectList = fakeList
    }

    override suspend fun getSubjects(): List<Subject> {
        return subjectList
    }

    override suspend fun addAndGetId(item: Subject): String =
        "Nothing"

    override fun addWithId(item: Subject, withId: String): Task<Void> =
        Mockito.mock(Task::class.java) as Task<Void>

    override suspend fun addUpVote(postId: String, userId: String) {
        subjectList[0].likers = listOf(FAKE_UID_1, FAKE_UID_2, userId)
    }

    override suspend fun removeUpVote(postId: String, userId: String) {
        subjectList[0].likers = listOf(FAKE_UID_1, FAKE_UID_2)
    }

    override suspend fun addDownVote(postId: String, userId: String) {
        subjectList[0].dislikers = listOf(FAKE_UID_3, FAKE_UID_4, userId)
    }

    override suspend fun removeDownVote(postId: String, userId: String) {
        subjectList[0].dislikers = listOf(FAKE_UID_3, FAKE_UID_4)
    }

    override suspend fun take(number: Long): QuerySnapshot =
        Mockito.mock(QuerySnapshot::class.java)

    override suspend fun getById(id: String): DocumentSnapshot =
        Mockito.mock(DocumentSnapshot::class.java)

    override fun remove(id: String): Task<Void> {
        val newList = arrayListOf<Subject>()

        for (r in subjectList) {
            if (r.getId() != id) {
                newList.add(r)
            }
        }

        subjectList = newList

        return Mockito.mock(Task::class.java) as Task<Void>
    }

    override fun add(item: Subject): Task<Void> =
        Mockito.mock(Task::class.java) as Task<Void>

    override fun update(id: String, transform: (Subject) -> Subject): Task<Transaction> =
        Mockito.mock(Task::class.java) as Task<Transaction>

    override fun transform(document: DocumentSnapshot): Subject? =
        document.toSubject()

    override fun query(): Query =
        Mockito.mock(Query::class.java)
}