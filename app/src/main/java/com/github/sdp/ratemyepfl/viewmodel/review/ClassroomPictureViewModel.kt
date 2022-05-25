package com.github.sdp.ratemyepfl.viewmodel.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.ui.activity.ReviewActivity
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.model.ImageFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectIndexed
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
            var posted: List<ImageFile> = listOf()
            imageStorage.getByDirectory(id)
                .collect {
                    posted = posted + it
                    pictures.postValue(posted)
                }
        }
    }

    fun uploadPicture(image: ImageFile) {
        viewModelScope.launch {
            imageStorage.addInDirectory(image, id)
            updatePicturesList()
        }
    }
}