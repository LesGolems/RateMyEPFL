package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.reviewable.EventRepository
import com.github.sdp.ratemyepfl.model.items.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * View model of the eventList activity. Bridge between the activity
 * and the database
 */
@HiltViewModel
class EventListViewModel @Inject constructor(private val repository: EventRepository) :
    ViewModel() {

    val events = MutableLiveData<List<Event>>()

    init {
        viewModelScope.launch {
            events.value = repository.getEvents()
        }
    }

    /**
     * Update the participation of the user and return if the update succeeded
     */
    fun updateRegistration(event: Event, userId: String): Boolean {
        var success: Boolean
        runBlocking {
            success = repository.updateParticipants(event.getId(), userId)
            updateEventsList()
        }
        return success
    }

    fun updateEventsList() {
        viewModelScope.launch {
            events.postValue(repository.getEvents())
        }
    }
}