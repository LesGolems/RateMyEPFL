package com.github.sdp.ratemyepfl.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.auth.GoogleAuthenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var user: ConnectedUser

    @Inject
    lateinit var auth: GoogleAuthenticator

    val isUserLoggedIn: LiveData<Boolean> = MutableLiveData(false)

    fun isUserLoggedIn() = isUserLoggedIn.value ?: false

    fun signOut(context: Context) {
        auth.signOut(context).addOnSuccessListener {
            (isUserLoggedIn as MutableLiveData).postValue((isUserLoggedIn.value)?.not())
        }
    }

    fun updateLoginStatus() {
        (isUserLoggedIn as MutableLiveData).postValue(user.isLoggedIn())
    }

}