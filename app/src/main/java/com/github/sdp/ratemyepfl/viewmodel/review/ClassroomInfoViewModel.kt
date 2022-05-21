package com.github.sdp.ratemyepfl.viewmodel.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.RoomNoiseRepository
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.model.NoiseInfo
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassroomInfoViewModel @Inject constructor(
    private val classroomRepo: ClassroomRepository,
    private val roomNoiseRepo: RoomNoiseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED_ID)!!

    val room = MutableLiveData<Classroom>()

    val noiseData = MutableLiveData<List<NoiseInfo>>()

    init {
        updateRoom()
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
                noiseData.postValue(roomNoiseInfo.noiseData.sortedBy { it.date }.reversed())
            }
        }
    }

    fun refresh() {
        updateRoom()
        refreshRoomNoise()
    }

    fun submitNoiseMeasure(measure: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            roomNoiseRepo.addMeasurement(id, TimeUtils.now(), measure)
        }
    }
}