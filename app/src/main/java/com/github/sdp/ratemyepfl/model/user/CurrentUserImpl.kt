package com.github.sdp.ratemyepfl.model.user

import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.google.firebase.auth.FirebaseAuth

class CurrentUserImpl(
    val user: ConnectedUser
) : CurrentUser {
    override fun getUsername(): String? {
        return FirebaseAuth.getInstance().currentUser?.displayName
    }

    override fun getProfilePictureUrl(): String? {
        return FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
    }

    override fun isLoggedIn(): Boolean {
        return user.isLoggedIn()
    }

    override fun getUserId(): String? {
        return user.getUserId()
    }

    override fun getEmail(): String? {
        return user.getEmail()
    }
}