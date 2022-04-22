package com.github.sdp.ratemyepfl.auth

import com.github.sdp.ratemyepfl.model.user.User
import javax.inject.Inject

/*
Fake connected user for tests
 */
class FakeConnectedUser @Inject constructor() : ConnectedUser {

    override fun isLoggedIn(): Boolean = instance != Instance.LOGGED_OUT

    override fun getUserId(): String? = instance.user?.uid

    override fun getEmail(): String? = if (instance != Instance.LOGGED_OUT) instance.user?.email else null

    override fun getUsername(): String? = if (instance != Instance.LOGGED_OUT) instance.user?.username else null

    enum class Instance(val user: User?) {
        LOGGED_OUT(null), FAKE_USER_1(fakeUser1), FAKE_USER_2(fakeUser2)
    }

    companion object {
        val fakeUser1 = User("12345", "John Smith", "john@example.com")

        val fakeUser2 = User("7", "Kylian Mbappe", "cmoiwesh@email.com")

        var instance = Instance.LOGGED_OUT
    }
}
