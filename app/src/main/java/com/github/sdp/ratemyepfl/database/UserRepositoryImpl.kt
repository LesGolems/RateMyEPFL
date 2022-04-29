package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.database.query.QueryResult.Companion.asQueryResult
import com.github.sdp.ratemyepfl.database.query.QueryResult.Companion.failure
import com.github.sdp.ratemyepfl.database.query.QueryResult.Companion.mapDocuments
import com.github.sdp.ratemyepfl.database.query.QueryResult.Companion.success
import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.model.user.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import kotlinx.coroutines.flow.map
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
        const val USERNAME_FIELD_NAME = "username"
        const val EMAIL_FIELD_NAME = "email"
        const val PICTURE_FIELD_NAME = "picture"

        fun DocumentSnapshot.toUser(): User {
            return User(
                uid = id,
                username = getString(USERNAME_FIELD_NAME),
                email = getString(EMAIL_FIELD_NAME),
            )
        }
    }


    /**
     * Retrieves a User object by their [uid].
     * Returns null in case of error.
     */
    override suspend fun getUserByUid(uid: String): User = repository
        .getById(uid)
        .toUser()

    private fun getBy(fieldName: String, value: String): QueryResult<List<User>> =
        repository
            .query()
            .match(fieldName, value)
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
                is QueryState.Failure -> QueryState.failure<User>(it.errorMessage)
                is QueryState.Loading -> QueryState.loading<User>()
                is QueryState.Success -> if (it.data.isEmpty())
                    QueryState.failure<User>("Unable to find a user with email $email")
                else QueryState.success<User>(it.data.first())
            }
        }.asQueryResult()

    override fun update(id: String, transform: (User) -> User): Task<Transaction> {
        return repository.update(id, transform)
    }

}