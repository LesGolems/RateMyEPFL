package com.github.sdp.ratemyepfl.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.backend.database.UserRepository
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val imageStorage: Storage<ImageFile>
) : ViewModel() {

    val topUsers = MutableLiveData<List<User>>()
    val topUsersPictures = MutableLiveData<List<ImageFile?>>()

    init {
        refreshUsers()
    }

    fun refreshUsers() {
        viewModelScope.launch {
            userRepo.getTopKarmaUsers().mapResult {
                topUsers.postValue(it)
            }.collect {}

            topUsersPictures.postValue(
                topUsers.value?.map {
                    it.uid.let { uid -> imageStorage.get(uid).last() }
                }
            )
        }
    }

}