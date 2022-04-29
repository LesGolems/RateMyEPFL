package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.UserRepositoryImpl.Companion.toUser
import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.model.user.User
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
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
    lateinit var db: FirebaseFirestore

    lateinit var userRepo: UserRepositoryImpl
    lateinit var repository: RepositoryImpl<User>

    @Before
    fun setup() = runTest {
        hiltRule.inject()
        repository = RepositoryImpl(db, UserRepositoryImpl.USER_COLLECTION_PATH) {
            it.toUser()
        }
        userRepo = UserRepositoryImpl(repository)
        repository.add(testUser).await()
    }

    @After
    fun clean() = runTest {
        repository.remove(testUser.uid).await()
    }


    @Test
    fun updateUserWorks() {
        runTest {
            val updateUser = testUser.copy(username = "newUsername")
            userRepo.update(testUser.getId()) { updateUser }
                .await()
            val user = userRepo.getUserByUid(testUser.uid)
            assertEquals(updateUser.uid, user.uid)
            assertEquals(updateUser.username, user.username)
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
            userRepo.getUsersByUsername(testUser.username!!)
                .collect {
                    when (it) {
                        is QueryState.Failure -> throw Exception("Should succeed")
                        is QueryState.Loading -> {}
                        is QueryState.Success -> {
                            assertEquals(false, it.data.isEmpty())
                            val user = it.data.first()
                            assertNotNull(user)
                            assertEquals(testUser.uid, user.uid)
                            assertEquals(testUser.username, user.username)
                            assertEquals(testUser.email, user.email)
                        }
                    }
                }
        }
    }

    @Test
    fun getUserByEmailWorks() {
        runTest {
            userRepo.getUserByEmail(testUser.email!!)
                .collect {
                    when (it) {
                        is QueryState.Failure -> throw Exception("Should succeed")
                        is QueryState.Loading -> {}
                        is QueryState.Success -> {
                            val user = it.data
                            assertNotNull(user)
                            assertEquals(testUser.uid, user.uid)
                            assertEquals(testUser.username, user.username)
                            assertEquals(testUser.email, user.email)
                        }
                    }
                }

        }
    }

}