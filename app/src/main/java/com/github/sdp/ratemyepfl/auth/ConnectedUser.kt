package com.github.sdp.ratemyepfl.auth

interface ConnectedUser {
    fun isLoggedIn(): Boolean

    fun getUserId(): String?

    fun getEmail(): String?
}