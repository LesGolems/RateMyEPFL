package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.EventRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventInfoViewModel @Inject constructor(
    private val eventRepo: EventRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED)!!

    val event = MutableLiveData<Event>()

    init {
        updateEvent()
    }

    fun updateEvent() {
        viewModelScope.launch {
            event.postValue(eventRepo.getEventById(id))
        }
    }

    fun updateRating(rating: ReviewRating) {
        viewModelScope.launch {
            eventRepo.updateEventRating(id, rating)
        }
    }
}