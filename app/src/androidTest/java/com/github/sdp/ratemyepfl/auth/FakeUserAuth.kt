package com.github.sdp.ratemyepfl.auth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import javax.inject.Inject

class FakeUserAuth @Inject constructor() : UserAuth {

    private var loggedIn = false

    override fun signIn(activity: AppCompatActivity) {
        loggedIn = true
    }

    override fun signOut(context: Context): Task<Void> {
        loggedIn = false
        return Tasks.forResult(null)
    }

    override fun isLoggedIn(): Boolean {
        return loggedIn
    }

    override fun getUserId(): String? {
        return "12345"
    }

    override fun getEmail(): String? {
        return "user@email.com"
    }
}