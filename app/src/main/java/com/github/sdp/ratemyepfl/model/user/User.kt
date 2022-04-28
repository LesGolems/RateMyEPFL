package com.github.sdp.ratemyepfl.model.user

import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.database.RepositoryItem
import com.github.sdp.ratemyepfl.database.UserRepositoryImpl
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String,
    val username: String?,
    val email: String?,
    val picture: String? = "$uid.jpg"
) : RepositoryItem {

    constructor(user: ConnectedUser) : this(
        uid = user.getUserId()!!,
        username = user.getUsername()!!,
        email = user.getEmail()!!,
    )

    override fun getId(): String = uid

    /**
     * Creates an hash map of the user
     */
    override fun toHashMap(): HashMap<String, Any?>{
        return hashMapOf(
            UserRepositoryImpl.USERNAME_FIELD_NAME to username,
            UserRepositoryImpl.EMAIL_FIELD_NAME to email,
        )
    }
}