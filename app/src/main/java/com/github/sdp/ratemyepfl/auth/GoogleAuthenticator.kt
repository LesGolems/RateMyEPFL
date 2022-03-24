package com.github.sdp.ratemyepfl.auth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import javax.inject.Inject


/*
Implementation of the authenticator using Google Sign In
*/
class GoogleAuthenticator @Inject constructor() : Authenticator {

    override fun signIn(activity: AppCompatActivity) {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        activity.startActivity(intent)
    }

    override fun signOut(context: Context): Task<Void> {
        return AuthUI.getInstance().signOut(context)
    }
}