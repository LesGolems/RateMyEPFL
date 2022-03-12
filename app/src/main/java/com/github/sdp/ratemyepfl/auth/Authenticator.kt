package com.github.sdp.ratemyepfl.auth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task

interface Authenticator {

    fun signIn(activity: AppCompatActivity)
    fun signOut(context: Context): Task<Void>

}