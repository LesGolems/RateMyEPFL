package com.github.sdp.ratemyepfl.backend.auth

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

/*
Implementation of the connected user using Firebase Auth
 */
class ConnectedUserImpl @Inject constructor() : ConnectedUser {

    override fun isLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    override fun getUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    override fun getEmail(): String? {
        return FirebaseAuth.getInstance().currentUser?.email
    }

    override fun getUsername(): String? {
        return FirebaseAuth.getInstance().currentUser?.displayName
    }
}