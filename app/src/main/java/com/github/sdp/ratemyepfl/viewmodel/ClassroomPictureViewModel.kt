package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.PictureRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ClassroomPictureViewModel @Inject constructor(
    private val pictureRepo: PictureRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Classroom Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED)!!

    // Photos
    val photos = MutableLiveData<List<Int>>()

    init {
        updatePhotosList()
    }

    fun updatePhotosList() {
        viewModelScope.launch {
            photos.postValue(pictureRepo.getPhotosByClassroomId(id))
        }
    }
}