package com.github.sdp.ratemyepfl.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassroomInfoViewModel @Inject constructor(
    private val classroomRepo: ClassroomRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED_ID)!!

    val room = MutableLiveData<Classroom>()

    init {
        updateRoom()
    }

    fun updateRoom() {
        viewModelScope.launch {
            room.postValue(classroomRepo.getRoomById(id))
        }
    }

}