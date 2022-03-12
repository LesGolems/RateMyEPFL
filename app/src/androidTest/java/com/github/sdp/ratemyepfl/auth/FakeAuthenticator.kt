package com.github.sdp.ratemyepfl.auth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import javax.inject.Inject

class FakeAuthenticator @Inject constructor(): Authenticator{

    override fun signIn(activity: AppCompatActivity) {
        FakeConnectedUser.loggedIn = true
    }

    override fun signOut(context: Context): Task<Void> {
        FakeConnectedUser.loggedIn = false
        return Tasks.forResult(null)
    }

}