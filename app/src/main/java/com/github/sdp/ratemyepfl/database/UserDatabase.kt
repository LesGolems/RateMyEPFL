package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.user.User
import com.github.sdp.ratemyepfl.model.user.User.Companion.toUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserDatabase private constructor() : UserRepository {

    /**
     * The UserDatabase follows the singleton principle
     */
    companion object {
        val instance = UserDatabase()
    }

    val USER_COLLECTION_PATH = "users"

    private fun collection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(USER_COLLECTION_PATH)
    }

    /**
     * Retrieves a User object by their [uid].
     * Returns null in case of error.
     */
    override suspend fun getUserByUid(uid: String): User? {
        val document = collection()
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
        return collection()
            .whereEqualTo(User.USERNAME_FIELD, username)
            .limit(50)
            .get()
            .await()
            .mapNotNull { obj -> obj.toUser() }
    }

    /**
     * Retrieves a User by its [email] address.
     */
    override suspend fun getUserByEmail(email: String): User {
        return collection()
            .whereEqualTo(User.EMAIL_FIELD, email)
            .get()
            .await()
            .mapNotNull { obj -> obj.toUser() }[0]
    }

    /**
     * Updates the [user] in the collection.
     * If the [user] isn't already part of it, it is added to the collection.
     */
    override suspend fun update(user: User) {
        collection()
            .document(user.uid)
            .set(user)
            .await()
    }

    /**
     * Deletes the User by its [uid]
     */
    override suspend fun delete(uid: String) {
        collection()
            .document(uid)
            .delete()
            .await()
    }

}