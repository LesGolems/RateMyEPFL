package com.github.sdp.ratemyepfl.auth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class UserAuthImpl @Inject constructor() : UserAuth {

    override fun signIn(activity : AppCompatActivity) {
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

    override fun signOut(context : Context) : Task<Void> {
        return AuthUI.getInstance().signOut(context)
    }

    override fun isLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    override fun getUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    override fun getEmail() : String? {
        return FirebaseAuth.getInstance().currentUser?.email
    }

}