package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.GradeInfoRepository
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassroomInfoViewModel @Inject constructor(
    private val classroomRepo: ClassroomRepository,
    private val gradeInfoRepo: GradeInfoRepository,
    private val savedStateHandle: SavedStateHandle
) : ReviewableInfoViewModel(gradeInfoRepo, savedStateHandle) {

    val room = MutableLiveData<Classroom>()

    init {
        refresh()
    }

    fun updateRoom() {
        viewModelScope.launch {
            room.postValue(classroomRepo.getRoomById(id))
        }
    }

    fun refresh() {
        updateRoom()
        refreshGrade()
    }
}