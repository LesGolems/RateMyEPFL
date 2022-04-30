package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.database.Storage
import com.github.sdp.ratemyepfl.database.UserRepositoryInterface
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
    val imageStorage: Storage<ImageFile>,
    val userDatabase: UserRepository
) : ViewModel() {

    private val picture: MutableLiveData<ImageFile?> = MutableLiveData(null)
    private val username: MutableLiveData<String?> = MutableLiveData(null)
    private val email: MutableLiveData<String?> = MutableLiveData(null)
    private val timetable: MutableLiveData<ArrayList<Class>?> = MutableLiveData(null)

    private var newUsername: String? = null
    private var newEmail: String? = null

    init {
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

        }
    }

    fun username(): MutableLiveData<String?> {
        return username
    }

    fun email(): MutableLiveData<String?> {
        return email
    }

    fun picture(): MutableLiveData<ImageFile?> {
        return picture
    }

    fun timetable(): MutableLiveData<ArrayList<Class>?> {
        return timetable
    }

    fun addClass(c: Class) {
        TODO()
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