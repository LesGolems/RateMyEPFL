package com.github.sdp.ratemyepfl.auth

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

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

}