package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.model.user.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction

interface UserRepository {

    suspend fun getUserByUid(uid: String): User?

    fun getUsersByUsername(username: String): QueryResult<List<User>>

    fun getUserByEmail(email: String): QueryResult<User>

    fun update(id: String, transform: (User) -> User): Task<Transaction>
}