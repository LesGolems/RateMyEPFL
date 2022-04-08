package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.user.User

interface UserRepositoryInterface {

    suspend fun getUserByUid(uid: String): User?

    suspend fun getUsersByUsername(username: String): List<User>

    suspend fun getUserByEmail(email: String): User

    suspend fun update(user: User)

    suspend fun delete(uid: String)
}