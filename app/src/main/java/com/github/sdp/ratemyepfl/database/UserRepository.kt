package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.CourseRepository.Companion.toCourse
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.user.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(db : FirebaseFirestore) : UserRepositoryInterface,
    Repository(db, USER_COLLECTION_PATH) {

    companion object {
        const val USER_COLLECTION_PATH = "users"
        const val USERNAME_FIELD = "username"
        const val EMAIL_FIELD = "email"
        const val PICTURE_FIELD = "picture"

        fun DocumentSnapshot.toUser(): User {
            return User(
                uid = id,
                username = getString(USERNAME_FIELD),
                email = getString(EMAIL_FIELD),
                picture = getString(PICTURE_FIELD)
            )
        }
    }

    fun toItem(snapshot: DocumentSnapshot): User? = snapshot.toUser()

    /**
     * Retrieves a User object by their [uid].
     * Returns null in case of error.
     */
    override suspend fun getUserByUid(uid: String): User? {
        val document = collection
            .document(uid)
            .get()
            .await()

        return if (document.exists()) {
            document.toUser()
        } else {
            null
        }
    }

    /**
     * Retrieves a list of Users with the same [username].
     */
    override suspend fun getUsersByUsername(username: String): List<User> {
        return collection
            .whereEqualTo(USERNAME_FIELD, username)
            .limit(50)
            .get()
            .await()
            .mapNotNull { obj -> obj.toUser() }
    }

    /**
     * Retrieves a User by its [email] address.
     */
    override suspend fun getUserByEmail(email: String): User {
        return collection
            .whereEqualTo(EMAIL_FIELD, email)
            .get()
            .await()
            .mapNotNull { obj -> obj.toUser() }[0]
    }

    /**
     * Updates the [user] in the collection.
     * If the [user] isn't already part of it, it is added to the collection.
     */
    override suspend fun update(user: User) {
        collection
            .document(user.uid)
            .set(user)
            .await()
    }

    /**
     * Deletes the User by its [uid]
     */
    override suspend fun delete(uid: String) {
        collection
            .document(uid)
            .delete()
            .await()
    }

}