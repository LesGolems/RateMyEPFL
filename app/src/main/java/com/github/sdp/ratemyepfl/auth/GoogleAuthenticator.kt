package com.github.sdp.ratemyepfl.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.github.sdp.ratemyepfl.activity.MainActivity
import com.google.android.gms.tasks.Task
import javax.inject.Inject


/*
Implementation of the authenticator using Google Sign In
*/
class GoogleAuthenticator @Inject constructor() : Authenticator{

    override fun signIn(resultLauncher: ActivityResultLauncher<Intent>){
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        resultLauncher.launch(intent)
    }

    override fun signOut(context: Context): Task<Void> {
        return AuthUI.getInstance().signOut(context)
    }
}