package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.database.query.QueryResult.Companion.asQueryResult
import com.github.sdp.ratemyepfl.database.query.QueryResult.Companion.mapDocuments
import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.model.items.Class
import com.github.sdp.ratemyepfl.model.user.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.google.gson.Gson
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl(private val repository: Repository<User>) : UserRepository {

    @Inject
    constructor(db: FirebaseFirestore) : this(
        LoaderRepositoryImpl(
            RepositoryImpl<User>(
                db,
                USER_COLLECTION_PATH
            ) {
                it.toUser()
            })
    )

    companion object {
        const val USER_COLLECTION_PATH = "users"
        const val USER_UID_FIELD_NAME = "uid"
        const val USERNAME_FIELD_NAME = "username"
        const val EMAIL_FIELD_NAME = "email"
        const val PICTURE_FIELD_NAME = "picture"
        const val TIMETABLE_FIELD_NAME = "timetable"

        fun DocumentSnapshot.toUser(): User? {
            val timetable = getString(TIMETABLE_FIELD_NAME)?.let {
                Gson().fromJson(it, Array<Class>::class.java).toCollection(ArrayList())
            } ?: User.DEFAULT_TIMETABLE
            return try {
                User.Builder(
                    uid = getString(USER_UID_FIELD_NAME),
                    username = getString(USERNAME_FIELD_NAME),
                    email = getString(EMAIL_FIELD_NAME),
                    timetable = timetable
                ).build()
            } catch (e: IllegalStateException) {
                null
            } catch (e: Exception) {
                throw DatabaseException("Cannot convert the document to a User (from $e \n ${e.stackTrace}")
            }
        }
    }


    /**
     * Retrieves a User object by their [uid].
     * Returns null in case of error.
     */
    override suspend fun getUserByUid(uid: String): User? = repository
        .getById(uid)
        .toUser()

    private fun getBy(fieldName: String, value: String): QueryResult<List<User>> =
        repository
            .query()
            .whereEqualTo(fieldName, value)
            .execute()
            .mapDocuments { it.toUser() }

    /**
     * Retrieves a list of Users with the same [username].
     */
    override fun getUsersByUsername(username: String): QueryResult<List<User>> = getBy(
        USERNAME_FIELD_NAME, username
    )

    /**
     * Retrieves a User by its [email] address.
     */
    override fun getUserByEmail(email: String): QueryResult<User> = getBy(EMAIL_FIELD_NAME, email)
        .map {
            when (it) {
                is QueryState.Failure -> QueryState.failure(it.error)
                is QueryState.Loading -> QueryState.loading()
                is QueryState.Success -> if (it.data.isEmpty())
                    QueryState.failure(DatabaseException("Unable to find a user with email $email"))
                else QueryState.success(it.data.first())
            }
        }.asQueryResult()

    override fun update(id: String, transform: (User) -> User): Task<Transaction> {
        return repository.update(id, transform)
    }

    override suspend fun register(user: User): QueryResult<Boolean> =
        QueryResult {
            if (getUserByUid(user.getId()) == null) {
                repository.add(user).await()
                emit(QueryState.success(false))
            } else {
                emit(QueryState.success(true))
            }
        }

}