package com.github.sdp.ratemyepfl.model.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CurrentUser : User {

    private fun currentUser() : FirebaseUser {
        return FirebaseAuth.getInstance().currentUser!!
    }

    override fun getUid(): String {
        return currentUser().uid
    }

    override fun getUsername(): String? {
        return currentUser().displayName
    }

    override fun getEmail(): String? {
        return currentUser().email
    }

}