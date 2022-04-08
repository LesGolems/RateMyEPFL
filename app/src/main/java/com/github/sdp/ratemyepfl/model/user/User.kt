package com.github.sdp.ratemyepfl.model.user

import com.github.sdp.ratemyepfl.auth.ConnectedUser
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String,
    val username: String?,
    val email: String?,
    val picture: String? = "$uid.jpg"
) {

    constructor(user: ConnectedUser) : this(
        uid = user.getUserId()!!,
        username = user.getUsername()!!,
        email = user.getEmail()!!,
        picture = user.getProfilePictureUrl()!!
    )
}