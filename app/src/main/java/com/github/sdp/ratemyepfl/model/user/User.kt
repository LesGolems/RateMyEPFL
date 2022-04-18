package com.github.sdp.ratemyepfl.model.user

import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.database.UserRepository
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String,
    val username: String?,
    val email: String?,
) {

    constructor(user: ConnectedUser) : this(
        uid = user.getUserId()!!,
        username = user.getUsername()!!,
        email = user.getEmail()!!,
    )

    /**
     * Creates an hash map of the user
     */
    fun toHashMap(): HashMap<String, Any?>{
        return hashMapOf(
            UserRepository.USERNAME_FIELD_NAME to username,
            UserRepository.EMAIL_FIELD_NAME to email,
            UserRepository.PICTURE_FIELD_NAME to "$uid.jpg"
        )
    }
}