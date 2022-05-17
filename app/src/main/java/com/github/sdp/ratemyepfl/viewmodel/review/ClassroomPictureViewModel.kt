package com.github.sdp.ratemyepfl.viewmodel.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.Storage
import com.github.sdp.ratemyepfl.model.ImageFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ClassroomPictureViewModel @Inject constructor(
    private val imageStorage: Storage<ImageFile>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Room id
    val id: String = savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED_ID)!!

    // Room pictures
    val pictures = MutableLiveData<List<ImageFile?>>()

    init {
        updatePicturesList()
    }

    fun updatePicturesList() {
        viewModelScope.launch {
            pictures.postValue(imageStorage.getByDirectory(id))
        }
    }

    fun uploadPicture(image: ImageFile) {
        viewModelScope.launch {
            imageStorage.addInDirectory(image, id)
            updatePicturesList()
        }
    }
}