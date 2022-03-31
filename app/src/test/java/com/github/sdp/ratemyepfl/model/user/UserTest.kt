package com.github.sdp.ratemyepfl.model.user

import com.github.sdp.ratemyepfl.auth.ConnectedUser
import org.junit.Assert.assertEquals
import org.junit.Test

class UserTest {

    @Test
    fun constructorWithAllFieldsWorks() {
        val user = User("12345", "Jean", "user@email.ch", "https://www.pictures.com/?q=coolpic")
        assertEquals("12345", user.uid)
        assertEquals("Jean", user.username)
        assertEquals("user@email.ch", user.email)
        assertEquals("https://www.pictures.com/?q=coolpic", user.picture)
    }

    @Test
    fun constructorWithLoggedInUserWorks() {
        val user = User(object : ConnectedUser {
            override fun getUsername(): String? {
                return "Jean"
            }

            override fun getProfilePictureUrl(): String? {
                return "https://www.pictures.com/?q=coolpic"
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
        assertEquals("https://www.pictures.com/?q=coolpic", user.picture)
    }

    @Test
    fun toHashMapWorks() {
        val userHash = User(
            "12345",
            "Jean",
            "user@email.ch",
            "https://www.pictures.com/?q=coolpic"
        ).toHashMap()
        assertEquals(userHash[User.USERNAME_FIELD], "Jean")
        assertEquals(userHash[User.EMAIL_FIELD], "user@email.ch")
        assertEquals(userHash[User.PICTURE_FIELD], "https://www.pictures.com/?q=coolpic")
    }
}