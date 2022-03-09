package com.github.sdp.ratemyepfl.auth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task

interface UserAuth {
    fun signIn(activity: AppCompatActivity)

    fun signOut(context: Context): Task<Void>

    fun isLoggedIn(): Boolean

    fun getUserId(): String?

    fun getEmail(): String?
}