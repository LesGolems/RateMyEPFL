package com.github.sdp.ratemyepfl.auth

import com.google.firebase.auth.FirebaseAuth

/*
Interface providing information about the current user
 */
interface ConnectedUser {
    /**
     * Return the user status
     *
     * @return true if the user is logged in, false otherwise
     */
    fun isLoggedIn(): Boolean

    /**
     * Return the user's id, or null if the user is not logged in
     *
     * @return the user id
     */
    fun getUserId(): String?

    /**
     * Return the user's email, or null if the user is not logged in
     */
    fun getEmail(): String?

    fun getUsername(): String?

    fun addCallBack(c: (FirebaseAuth) -> Unit)
}