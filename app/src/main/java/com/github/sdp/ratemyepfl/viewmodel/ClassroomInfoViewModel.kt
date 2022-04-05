package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.ClassroomRepositoryInterface
import com.github.sdp.ratemyepfl.database.ReviewRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Classroom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassroomInfoViewModel @Inject constructor(
    private val reviewRepo: ReviewRepositoryInterface,
    private val roomRepo: ClassroomRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ReviewViewModel(
    reviewRepo, savedStateHandle
) {
    val room = MutableLiveData<Classroom>()

    init {
        updateRoom()
    }

    fun updateRoom() {
        viewModelScope.launch {
            room.postValue(roomRepo.getRoomById(id))
        }
    }
}