package com.github.sdp.ratemyepfl.auth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class GoogleAuthenticator @Inject constructor() : Authenticator{

    override fun signIn(activity: AppCompatActivity) {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        activity.startActivity(signInIntent)
    }

    override fun signOut(context: Context): Task<Void> {
        return AuthUI.getInstance().signOut(context)
    }
}