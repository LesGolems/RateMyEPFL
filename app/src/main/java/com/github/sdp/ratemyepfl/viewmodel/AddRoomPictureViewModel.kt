package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.Storage
import com.github.sdp.ratemyepfl.model.ImageFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class AddRoomPictureViewModel @Inject constructor(
    private val imageStorage: Storage<ImageFile>,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun submitPicture(picture: ImageFile, roomId: String) {
        viewModelScope.launch {
            imageStorage.addInDirectory(picture, roomId)
        }
    }
}