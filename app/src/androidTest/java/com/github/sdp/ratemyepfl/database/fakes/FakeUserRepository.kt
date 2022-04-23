package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.UserRepositoryInterface
import com.github.sdp.ratemyepfl.model.user.User
import javax.inject.Inject

class FakeUserRepository @Inject constructor() : UserRepositoryInterface {

    private val users = HashMap<String, User>()

    init {
        users.put("56789", User("56789", "Marc", "Marc.Antoine@gmail.com"))
        users.put("18189", User("18189", "Charolais", "Etienne.cdp@gmail.com"))
        users.put("30220", User("30220", "x_sasuke9", "Celestin.Renaut@gmail.com"))
        users.put("12345", User("12345", "John Smith", "john@example.com"))
    }

    override suspend fun getUserByUid(uid: String): User? {
        if (users.containsKey(uid)) {
            return users.get(uid)
        }
        return null
    }

    override suspend fun getUsersByUsername(username: String): List<User> {
        return users.filterValues { user -> user.username.equals(username) }.values.toList()
    }

    override suspend fun getUserByEmail(email: String): User {
        return users.filterValues { user -> user.email.equals(email) }.values.toList()[0]
    }

    override suspend fun update(user: User) {
        users.put(user.uid, user)
    }

}