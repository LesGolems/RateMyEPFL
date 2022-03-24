package com.github.sdp.ratemyepfl.auth

/*
Interface providing information about the current user
 */
interface ConnectedUser {
    fun isLoggedIn(): Boolean

    fun getUserId(): String?

    fun getEmail(): String?

    fun getUsername(): String?

    fun getProfilePictureUrl(): String?
}