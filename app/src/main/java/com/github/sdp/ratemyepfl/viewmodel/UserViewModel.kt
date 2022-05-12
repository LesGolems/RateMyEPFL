package com.github.sdp.ratemyepfl.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.auth.GoogleAuthenticator
import com.github.sdp.ratemyepfl.database.Storage
import com.github.sdp.ratemyepfl.database.UserRepository
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    val currentUser: ConnectedUser,
    val auth: GoogleAuthenticator,
    val imageStorage: Storage<ImageFile>,
    val userDatabase: UserRepository
) : ViewModel() {

    val user: MutableLiveData<User?> = MutableLiveData(null)
    val picture: MutableLiveData<ImageFile?> = MutableLiveData(null)
    val isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        refreshUser()
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            auth.signOut(context).await()
            refreshUser()
        }
    }

    /**
     * Refreshes the user profile, if the user is not connected set the username to
     * visitor
     */
    fun refreshUser() {
        isUserLoggedIn.postValue(currentUser.isLoggedIn())

        val uid = currentUser.getUserId()
        if (uid != null) {
            viewModelScope.launch {
                userDatabase.getUserByUid(uid)?.let { user.postValue(it) }
                imageStorage.get(uid)?.let { picture.postValue(it) }
            }
        } else {
            user.postValue(null)
            picture.postValue(null)
        }
    }
}