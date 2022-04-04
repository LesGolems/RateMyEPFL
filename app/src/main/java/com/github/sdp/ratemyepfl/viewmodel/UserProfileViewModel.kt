package com.github.sdp.ratemyepfl.viewmodel

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.database.Storage
import com.github.sdp.ratemyepfl.database.UserRepository
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.user.User
import kotlinx.coroutines.launch

class UserProfileViewModel(
    val currentUser: ConnectedUser,
    val imageStorage: Storage<ImageFile>,
    val userDatabase: UserRepository
    ) : ViewModel() {

    private val picture : MutableLiveData<ImageFile?> = MutableLiveData(null)
    private var username : MutableLiveData<String?> = MutableLiveData(null)
    private var email : MutableLiveData<String?> = MutableLiveData(null)
    private var nextUsername : String? = null
    private var nextEmail : String? = null

    init {
        if (currentUser.isLoggedIn()) {

            viewModelScope.launch {
                val user = currentUser.getUserId()?.let { userDatabase.getUserByUid(it) }
                if (user != null) {
                    username.postValue(user.username)
                    email.postValue(user.email)
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
        }
    }

    fun username() : MutableLiveData<String?> {
        return username
    }

    fun email() : MutableLiveData<String?> {
        return email
    }

    fun picture() : MutableLiveData<ImageFile?> {
        return picture
    }

    fun changeUsername(newUsername: String) {
        if (newUsername.length > 12) {
            throw IllegalArgumentException("$newUsername should be at most 12 characters long.")
        } else if (newUsername.length < 4) {
            throw IllegalArgumentException("Username should be at least 4 characters long.")
        } else if (username.value != newUsername) {
            nextUsername = newUsername
        }
    }

    fun changeEmail(newEmail: String) {
        if (newEmail.split("@").size != 2) {
            throw IllegalArgumentException("Wrong email format.")
        } else if (email.value != newEmail) {
            nextEmail = newEmail
        }
    }

    fun changeProfilePicture(newImage: ImageFile) {
        if (newImage.size > imageStorage.MAX_ITEM_SIZE) {
            throw IllegalArgumentException("Picture size should be less than ${imageStorage.MAX_ITEM_SIZE}.")
        }
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
                picture.value?.let { imageStorage.put(it) }
            }
            viewModelScope.launch {
                val id = currentUser.getUserId()
                val user = User(id!!, nextUsername, nextEmail, picture.value?.id + ".jpg")
                userDatabase.update(user)
                username.postValue(nextUsername)
                email.postValue(nextEmail)
            }
        }
    }
}
