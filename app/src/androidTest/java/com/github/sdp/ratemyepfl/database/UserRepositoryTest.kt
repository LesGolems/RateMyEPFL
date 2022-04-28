package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.user.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class UserRepositoryTest {
    private val testUser = User("Fake uid", "username", "email")

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userRepo: UserRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
        userRepo.add(testUser)
    }

    @After
    fun clean() {
        //userRepo.remove(testUser.uid)
    }

    @Test
    fun updateUserWorks(){
        runTest {
            userRepo.update(User("Fake uid", "username new", "email"))
            val user = userRepo.getUserByUid(testUser.uid)
            assertEquals(testUser.uid, user!!.uid, )
            assertEquals("username new", user.username)
        }
    }

    @Test
    fun getUserByIdWorks() {
        runTest {
            val user = userRepo.getUserByUid(testUser.uid)
            assertNotNull(user)
            assertEquals(testUser.uid, user!!.uid)
            assertEquals(testUser.username, user.username)
            assertEquals(testUser.email, user.email)
        }
    }

    @Test
    fun getUserByUsernameWorks() {
        runTest {
            val user = userRepo.getUsersByUsername(testUser.username!!)[0]
            assertNotNull(user)
            assertEquals(testUser.uid, user.uid)
            assertEquals(testUser.username, user.username)
            assertEquals(testUser.email, user.email)
        }
    }

    @Test
    fun getUserByEmailWorks() {
        runTest {
            val user = userRepo.getUserByEmail(testUser.email!!)
            assertNotNull(user)
            assertEquals(testUser.uid, user.uid)
            assertEquals(testUser.username, user.username)
            assertEquals(testUser.email, user.email)
        }
    }

}