package com.github.sdp.ratemyepfl.database

import android.util.Log
import com.github.sdp.ratemyepfl.model.user.User
import com.github.sdp.ratemyepfl.model.user.User.Companion.toUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDatabase constructor() : UserRepository {

    companion object {
        val instance = UserDatabase()
    }

    val USER_COLLECTION_PATH = "users"

    private fun collection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(USER_COLLECTION_PATH)
    }

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

    override suspend fun getUsersByUsername(username: String): List<User> {
        return collection()
            .whereEqualTo(User.USERNAME_FIELD, username)
            .limit(50)
            .get()
            .await()
            .mapNotNull { obj -> obj.toUser() }
    }

    override suspend fun getUserByEmail(email: String): User {
        return collection()
            .whereEqualTo(User.EMAIL_FIELD, email)
            .get()
            .await()
            .mapNotNull { obj -> obj.toUser() }[0]
    }

    override suspend fun update(user: User) {
        collection()
            .document(user.uid)
            .set(user.toHashMap())
            .await()
    }

    override suspend fun delete(uid: String) {
        collection()
            .document(uid)
            .delete()
            .await()
    }

}