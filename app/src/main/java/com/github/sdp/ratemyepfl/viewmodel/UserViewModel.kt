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

    private val noUser = User.Builder("uid", "Visitor", "You are not logged in", null, null).build()

    init {
        refreshProfile()
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            auth.signOut(context).await()
            refreshProfile()
        }
    }

    /**
     * Refreshes the user profile, if the user is not connected set the username to
     * visitor
     */
    fun refreshProfile() {
        isUserLoggedIn.postValue(currentUser.isLoggedIn())

        if (currentUser.isLoggedIn()) {

            viewModelScope.launch {
                val userDB = currentUser.getUserId()?.let { userDatabase.getUserByUid(it) }
                if (userDB != null) {
                    user.postValue(userDB)
                }

                val imageFile = currentUser.getUserId()?.let { imageStorage.get(it) }
                picture.postValue(imageFile)
            }

        } else {
            user.postValue(noUser)
            picture.postValue(null)
        }
    }
}