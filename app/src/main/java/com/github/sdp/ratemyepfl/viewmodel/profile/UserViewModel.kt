package com.github.sdp.ratemyepfl.viewmodel.profile

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.backend.auth.GoogleAuthenticator
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.backend.database.UserRepository
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.items.Class
import com.github.sdp.ratemyepfl.model.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val currentUser: ConnectedUser,
    val auth: GoogleAuthenticator,
    val imageStorage: Storage<ImageFile>,
    private val userDatabase: UserRepository
) : ViewModel() {

    val user: MutableLiveData<User?> = MutableLiveData(null)
    val picture: MutableLiveData<ImageFile?> = MutableLiveData(null)
    val isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData(false)

    private var newUsername: String? = null
    private var newEmail: String? = null

    init {
        refreshUser()
    }

    fun loadImage(): Flow<ImageFile> =
        currentUser.getUserId()?.let {
            imageStorage.get(it)
        } ?: throw DisconnectedUserException("Cannot fetch the image of a disconnected user")


    /**
     * Refreshes the user profile, if the user is not connected set the user and picture to null
     */
    fun refreshUser() {
        isUserLoggedIn.postValue(currentUser.isLoggedIn())

        val uid = currentUser.getUserId()
        if (uid != null) {
            viewModelScope.launch {
                userDatabase.getUserByUid(uid)?.let { user.postValue(it) }
                imageStorage.get(uid).lastOrNull()?.let { picture.postValue(it) }
            }
        } else {
            user.postValue(null)
            picture.postValue(null)
        }
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            auth.signOut(context).await()
            refreshUser()
        }
    }

    /**
     * Adds the class to the user's timetable
     */
    fun addClass(c: Class) {
        if (currentUser.isLoggedIn()) {
            viewModelScope.launch {
                userDatabase.updateTimetable(currentUser.getUserId(), c)
            }
        }
    }

    fun changeUsername(newUsername: String) {
        if (newUsername.length > 12) {
            throw IllegalArgumentException("$newUsername should be at most 12 characters long.")
        } else if (newUsername.length < 4) {
            throw IllegalArgumentException("Username should be at least 4 characters long.")
        }
        this.newUsername = newUsername
    }

    fun changeEmail(newEmail: String) {
        if (newEmail.split("@").size != 2) {
            throw IllegalArgumentException("Wrong email format.")
        }
        this.newEmail = newEmail
    }

    fun discardChanges() {
        user.postValue(user.value)
        picture.postValue(picture.value)
    }

    fun submitChanges() {
        if (currentUser.isLoggedIn()) {
            viewModelScope.launch {
                picture.value?.let { imageStorage.add(it) }
            }
            viewModelScope.launch {
                val id = currentUser.getUserId()!!
                user.postValue(user.value?.copy(username = newUsername, email = newEmail))
                if (newUsername != null && newEmail != null) {
                    userDatabase.update(id) {
                        it.copy(username = newUsername, email = newEmail)
                    }.collect()
                }
            }
        } else {
            throw DisconnectedUserException()
        }
    }
}