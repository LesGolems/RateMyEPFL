package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.user.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(db: FirebaseFirestore) : UserRepositoryInterface {

    val repository = RepositoryImpl(db, USER_COLLECTION_PATH)

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
                picture = getString(PICTURE_FIELD_NAME)
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

    private suspend fun getBy(fieldName: String, value: String): List<User> =
        repository
            .collection
            .whereEqualTo(fieldName, value)
            .get()
            .await()
            .mapNotNull { obj -> obj.toUser() }

    /**
     * Retrieves a list of Users with the same [username].
     */
    override suspend fun getUsersByUsername(username: String): List<User> = getBy(
        USERNAME_FIELD_NAME, username
    )

    /**
     * Retrieves a User by its [email] address.
     */
    override suspend fun getUserByEmail(email: String): User = getBy(EMAIL_FIELD_NAME, email)[0]

    /**
     * Updates the [user] in the collection.
     * If the [user] isn't already part of it, it is added to the collection.
     */
    override suspend fun update(user: User) {
        repository
            .collection
            .document(user.uid)
            .set(user.toHashMap())
            .await()
    }

    /**
     * Add the user to the DB
     */
    fun add(user: User) {
        repository
            .collection
            .document(user.uid)
            .set(user.toHashMap())
    }
}