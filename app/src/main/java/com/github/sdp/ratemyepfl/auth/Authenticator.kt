package com.github.sdp.ratemyepfl.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task

/*
Interface representing the app authenticator, used to log in and log out
 */
interface Authenticator {

    fun signIn(resultLauncher: ActivityResultLauncher<Intent>)
    fun signOut(context: Context): Task<Void>

}