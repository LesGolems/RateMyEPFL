package com.github.sdp.ratemyepfl.auth

interface UserAuth {
    fun signIn()

    fun signOut()

    fun isLoggedIn()

    fun getEmail()
}