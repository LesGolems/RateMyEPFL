package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.GradeInfoRepository
import com.github.sdp.ratemyepfl.database.RoomNoiseRepository
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class ClassroomInfoViewModel @Inject constructor(
    private val classroomRepo: ClassroomRepository,
    private val gradeInfoRepo: GradeInfoRepository,
    private val roomNoiseRepo: RoomNoiseRepository,
    private val savedStateHandle: SavedStateHandle
) : ReviewableInfoViewModel(gradeInfoRepo, savedStateHandle) {

    val room = MutableLiveData<Classroom>()

    companion object {
        private val EPFL_ZONE_ID = ZoneId.of("Europe/Zurich")
    }

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

    fun submitNoiseMeasure(measure: Double) {
        val date = LocalDateTime.now(EPFL_ZONE_ID).truncatedTo(ChronoUnit.SECONDS)
        val measurement = String.format("%.1f", measure).toDouble()

        viewModelScope.launch(Dispatchers.IO) {
            roomNoiseRepo.addMeasurement(id, date, measurement)
        }
    }
}