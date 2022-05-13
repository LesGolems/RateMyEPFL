package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.GradeInfoRepository
import com.github.sdp.ratemyepfl.database.RoomNoiseRepository
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.model.RoomNoiseInfo
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassroomInfoViewModel @Inject constructor(
    private val classroomRepo: ClassroomRepository,
    private val gradeInfoRepo: GradeInfoRepository,
    private val roomNoiseRepo: RoomNoiseRepository,
    private val savedStateHandle: SavedStateHandle
) : ReviewableInfoViewModel(gradeInfoRepo, savedStateHandle) {

    val room = MutableLiveData<Classroom>()

    val noiseData = MutableLiveData<Map<String, Int>>()

    init {
        refresh()
    }

    private fun updateRoom() {
        viewModelScope.launch {
            room.postValue(classroomRepo.getRoomById(id))
        }
    }

    private fun refreshRoomNoise() {
        viewModelScope.launch {
            val roomNoiseInfo = roomNoiseRepo.getRoomNoiseInfoById(id)
            if (roomNoiseInfo != null) {
                noiseData.postValue(roomNoiseInfo.noiseData)
            }
        }
    }

    fun refresh() {
        updateRoom()
        refreshGrade()
        refreshRoomNoise()
    }

    fun submitNoiseMeasure(measure: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            roomNoiseRepo.addMeasurement(id, TimeUtils.now(), measure)
        }
    }
}