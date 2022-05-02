package com.github.sdp.ratemyepfl.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.auth.GoogleAuthenticator
import com.github.sdp.ratemyepfl.database.Storage
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.database.UserRepository
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.items.Class
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    val currentUser: ConnectedUser,
    val auth: GoogleAuthenticator,
    val imageStorage: Storage<ImageFile>,
    val userDatabase: UserRepository
) : ViewModel() {

    val picture: MutableLiveData<ImageFile?> = MutableLiveData(null)
    val username: MutableLiveData<String?> = MutableLiveData(null)
    val email: MutableLiveData<String?> = MutableLiveData(null)
    val timetable: MutableLiveData<ArrayList<Class>?> = MutableLiveData(null)
    val isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData(false)

    private var newUsername: String? = null
    private var newEmail: String? = null

    init {
        refreshProfile()
    }

    fun addClass(c: Class) {
        TODO()
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
                val user = currentUser.getUserId()?.let { userDatabase.getUserByUid(it) }
                if (user != null) {
                    username.postValue(user.username)
                    email.postValue(user.email)
                    timetable.postValue(user.timetable)
                } else {
                    username.postValue(currentUser.getUsername()!!.split(" ")[0])
                    email.postValue(currentUser.getEmail())
                }
            }

            viewModelScope.launch {
                val imageFile = currentUser.getUserId()?.let { imageStorage.get(it) }
                if (imageFile != null) {
                    picture.postValue(imageFile)
                }
            }

        } else {
            username.postValue("Visitor")
            email.postValue("You are not logged in")
            picture.postValue(null)
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

    fun changeProfilePicture(newImage: ImageFile) {
        picture.postValue(newImage)
    }

    fun discardChanges() {
        username.postValue(username.value)
        email.postValue(email.value)
        picture.postValue(picture.value)
    }

    fun submitChanges() {
        if (currentUser.isLoggedIn()) {
            viewModelScope.launch {
                picture.value?.let { imageStorage.add(it) }
            }
            viewModelScope.launch {
                val id = currentUser.getUserId()
                newUsername?.let { username.postValue(it) }
                newEmail?.let { email.postValue(it) }
                if (newUsername != null && newEmail != null && id != null) {
                    userDatabase.update(id) {
                        it.copy(username = newUsername, email = newEmail)
                    }.await()
                }
            }
        } else {
            throw DisconnectedUserException()
        }
    }
}