package com.github.sdp.ratemyepfl.auth

import javax.inject.Inject

/*
Fake connected user for tests
 */
class FakeConnectedUser @Inject constructor(): ConnectedUser {

    override fun isLoggedIn(): Boolean = loggedIn

    override fun getUserId(): String? = "12345"

    override fun getEmail(): String? = "user@email.com"

    companion object {
        var loggedIn = false
    }
}
