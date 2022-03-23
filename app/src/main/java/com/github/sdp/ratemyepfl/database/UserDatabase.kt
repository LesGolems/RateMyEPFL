package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.user.User
import com.github.sdp.ratemyepfl.model.user.User.Companion.toUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDatabase @Inject constructor(): UserRepository, Repository() {

    val collection = db.collection("users")

    override suspend fun getUserByUid(uid: String): User {
        return collection.document(uid)
            .get()
            .await()
            .toUser()
    }

    override suspend fun getUsersByUsername(username: String): List<User> {
        return collection.whereEqualTo("username", username)
            .limit(50)
            .get()
            .await()
            .mapNotNull { obj -> obj.toUser() }
    }

    override suspend fun getUserByEmail(email: String): User {
        return collection.whereEqualTo("email", email)
            .get()
            .await()
            .mapNotNull { obj -> obj.toUser() }
            .get(0)
    }

    override suspend fun update(user: User) {
        collection.document(user.getUid())
            .set(user.toHashMap())
    }

    override suspend fun delete(uid: String) {
        collection.document(uid)
            .delete()
    }

}