package com.github.sdp.ratemyepfl.backend.database

import com.github.sdp.ratemyepfl.backend.database.query.QueryResult
import com.github.sdp.ratemyepfl.model.items.Class
import com.github.sdp.ratemyepfl.model.user.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import kotlinx.coroutines.flow.Flow

interface UserRepository : Repository<User> {

    suspend fun getUserByUid(uid: String): User

    fun getUsersByUsername(username: String): QueryResult<List<User>>

    fun getUserByEmail(email: String): QueryResult<User>

    /**
     * Register the provided [user] if it is not registered yet. Returns true
     * if the user was already registered, false otherwise.
     *
     * @param user: the user to register
     *
     */
    suspend fun register(user: User): Flow<Boolean>


    /**
     * Updates the karma of user with [uid]
     */
    suspend fun updateKarma(uid: String?, inc: Int)


    suspend fun updateTimetable(uid: String?, c: Class)

}