package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.GradeInfoRepository
import com.github.sdp.ratemyepfl.database.reviewable.EventRepository
import com.github.sdp.ratemyepfl.model.items.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventInfoViewModel @Inject constructor(
    private val eventRepo: EventRepository,
    private val gradeInfoRepo: GradeInfoRepository,
    private val savedStateHandle: SavedStateHandle
) : ReviewableInfoViewModel(gradeInfoRepo, savedStateHandle) {

    val event = MutableLiveData<Event>()

    init {
        refresh()
    }

    fun updateEvent() {
        viewModelScope.launch {
            event.postValue(eventRepo.getEventById(id))
        }
    }

    fun refresh() {
        updateEvent()
        refreshGrade()
    }
}