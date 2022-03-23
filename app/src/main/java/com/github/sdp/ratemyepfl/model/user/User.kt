package com.github.sdp.ratemyepfl.model.user

import com.google.firebase.firestore.DocumentSnapshot

interface User {

    companion object {
        fun DocumentSnapshot.toUser() : User {
            return object : User {
                override fun getUid(): String {
                    return getString("uid")!!
                }

                override fun getUsername(): String? {
                    return getString("username")
                }

                override fun getEmail(): String? {
                    return getString("email")
                }
            }
        }
    }

    fun toHashMap(): HashMap<String, String?> {
        return hashMapOf(
            "username" to getUsername(),
            "email" to getEmail()
        )
    }

    fun getUid() : String

    fun getUsername() : String?

    fun getEmail() : String?
}