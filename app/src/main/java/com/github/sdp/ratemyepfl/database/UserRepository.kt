package com.github.sdp.ratemyepfl.database

import android.os.AsyncTask
import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.model.user.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.asTask

interface UserRepository {

    suspend fun getUserByUid(uid: String): User?

    fun getUsersByUsername(username: String): QueryResult<List<User>>

    fun getUserByEmail(email: String): QueryResult<User>

    fun update(id: String, transform: (User) -> User): Task<Transaction>

    /**
     * Register the provided [user] if it is not registered yet. Returns true
     * if the user was already registered, false otherwise.
     *
     * @param user: the user to register
     *
     */
    suspend fun register(user: User): Task<Boolean>

}