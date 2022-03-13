package com.github.sdp.ratemyepfl.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import javax.inject.Inject

/*
Fake authenticator for tests
 */
class FakeAuthenticator @Inject constructor(): Authenticator{

    override fun signIn(resultLauncher: ActivityResultLauncher<Intent>) {
        FakeConnectedUser.loggedIn = true
    }

    override fun signOut(context: Context): Task<Void> {
        FakeConnectedUser.loggedIn = false
        return Tasks.forResult(null)
    }

}