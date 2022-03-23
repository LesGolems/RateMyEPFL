package com.github.sdp.ratemyepfl.auth

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
}