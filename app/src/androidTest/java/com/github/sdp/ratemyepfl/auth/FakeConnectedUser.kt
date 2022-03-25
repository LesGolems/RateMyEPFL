package com.github.sdp.ratemyepfl.auth

import javax.inject.Inject

/*
Fake connected user for tests
 */
class FakeConnectedUser @Inject constructor() : ConnectedUser {

    override fun isLoggedIn(): Boolean = loggedIn

    override fun getUserId(): String? = userId

    override fun getEmail(): String? = if (isLoggedIn()) email else null

    override fun getUsername(): String? {
        return null
    }

    override fun getProfilePictureUrl(): String? {
        return null
    }

    companion object {
        var loggedIn: Boolean = false
        var email: String? = "user@email.com"
        var userId: String? = "12345"
    }
}
