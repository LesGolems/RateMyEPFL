package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.database.UserRepository
import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.model.items.Class
import com.github.sdp.ratemyepfl.model.user.User
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.Transaction
import org.mockito.Mockito
import javax.inject.Inject

class FakeUserRepository @Inject constructor() : UserRepository {


    companion object {
        val timetable: ArrayList<Class> =
            arrayListOf<Class>(Class(1, "Bamboula", "René", "bc", 0, 10, 12),
                Class(2,"pain","singe","in",1,8,14))
        val UID1 = "56789"
        val UID2 = "18189"
        val UID3 = "30220"
        val UID4 = "12345"

        val userMap = mapOf(
            UID1 to User(UID1, "Marc", "Marc.Antoine@gmail.com", timetable = ArrayList<Class>()),
            UID2 to User(
                UID2,
                "Charolais",
                "Etienne.cdp@gmail.com",
                timetable = ArrayList<Class>()
            ),
            UID3 to User(
                UID3,
                "x_sasuke9",
                "Celestin.Renaut@gmail.com",
                timetable = ArrayList<Class>()
            ),
            UID4 to User("12345", "John Smith", "john@example.com", timetable = timetable)
        )
    }

    val users = userMap.toMutableMap()

    override suspend fun getUserByUid(uid: String): User? {
        if (users.containsKey(uid)) {
            return users.get(uid)
        }
        return null
    }

    override fun getUsersByUsername(username: String): QueryResult<List<User>> {
        return QueryResult.success(users.filterValues { user -> user.username.equals(username) }.values.toList())
    }

    override fun getUserByEmail(email: String): QueryResult<User> {
        return QueryResult.success(users.filterValues { user -> user.email.equals(email) }.values.toList()[0])
    }

    @Suppress("UNCHECKED_CAST")
    override fun update(id: String, transform: (User) -> User): Task<Transaction> {
        return Mockito.mock(Task::class.java) as Task<Transaction>
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun register(user: User): Task<Boolean> = Tasks.forResult(true)

    @Suppress("UNCHECKED_CAST")
    override fun updateKarma(uid: String, inc: Int): Task<Transaction> {
        return Mockito.mock(Task::class.java) as Task<Transaction>
    }

}