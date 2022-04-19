package com.github.sdp.ratemyepfl.model.user

import com.github.sdp.ratemyepfl.auth.ConnectedUser
import org.junit.Assert.assertEquals
import org.junit.Test

class UserTest {

    @Test
    fun constructorWithAllFieldsWorks() {
        val user = User("12345", "Jean", "user@email.ch")
        assertEquals("12345", user.uid)
        assertEquals("Jean", user.username)
        assertEquals("user@email.ch", user.email)
    }

    @Test
    fun constructorWithLoggedInUserWorks() {
        val user = User(object : ConnectedUser {
            override fun getUsername(): String? {
                return "Jean"
            }

            override fun isLoggedIn(): Boolean {
                return true
            }

            override fun getUserId(): String? {
                return "12345"
            }

            override fun getEmail(): String? {
                return "user@email.ch"
            }
        })
        assertEquals("12345", user.uid)
        assertEquals("Jean", user.username)
        assertEquals("user@email.ch", user.email)
    }
}