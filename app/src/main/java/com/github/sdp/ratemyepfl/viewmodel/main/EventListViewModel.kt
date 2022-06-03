package com.github.sdp.ratemyepfl.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.backend.database.UserRepository
import com.github.sdp.ratemyepfl.backend.database.reviewable.EventRepository
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.post.EventWithAuthor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * View model of the eventList activity. Bridge between the activity
 * and the database
 */
@HiltViewModel
class EventListViewModel @Inject constructor(
    private val repository: EventRepository,
    private val userRepo: UserRepository,
    private val imageStorage: Storage<ImageFile>
) :
    ViewModel() {

    val eventsWithAuthors = MutableLiveData<List<EventWithAuthor>>()
    val events = MutableLiveData<List<Event>>()

    init {
        updateEventsList()
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
            events.value = repository.getEvents()
            eventsWithAuthors.value = events.value?.map { event ->
                EventWithAuthor(event,
                    event.creator.let { userRepo.getUserByUid(it) },
                    event.creator.let { imageStorage.get(it).lastOrNull() })
            }
        }
    }
}