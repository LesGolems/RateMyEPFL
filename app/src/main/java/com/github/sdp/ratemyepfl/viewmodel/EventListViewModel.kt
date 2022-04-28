package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.EventRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model of the eventList activity. Bridge between the activity
 * and the database
 */
@HiltViewModel
class EventListViewModel @Inject constructor(private val repository: EventRepositoryInterface) :
    ViewModel() {

    val events = MutableLiveData<List<Event>>()

    init {
        viewModelScope.launch {
            events.value = repository.getEvents()
        }
    }

    /**
     * Increment the participation
     */
    fun register(event: Event) {
        viewModelScope.launch {
            repository.incrementParticipants(event.id)
            updateEventsList()
        }
    }

    /**
     * Decrement the participation
     */
    fun unregister(event: Event) {
        viewModelScope.launch {
            repository.decrementParticipants(event.id)
            updateEventsList()
        }
    }

    fun updateEventsList() {
        viewModelScope.launch {
            events.postValue(repository.getEvents())
        }
    }
}