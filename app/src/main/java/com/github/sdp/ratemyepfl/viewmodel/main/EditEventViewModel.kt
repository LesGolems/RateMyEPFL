package com.github.sdp.ratemyepfl.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.backend.database.reviewable.EventRepository
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.time.*
import com.github.sdp.ratemyepfl.ui.activity.EditEventActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditEventViewModel @Inject constructor(
    private val eventRepo: EventRepository,
    private val connectedUser: ConnectedUser,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val NO_TITLE_MESSAGE: String = "Please enter a title"
        const val NO_LIM_PART_MESSAGE: String = "Please select a limit of participants"
        const val NO_DATE_MESSAGE: String = "Please select a date"
        const val NO_TIME_MESSAGE: String = "Please select a time"
        const val NO_LOCATION_MESSAGE: String = "Please select a location"
    }

    val isNewEvent: Boolean =
        savedStateHandle.get<Boolean>(EditEventActivity.EXTRA_IS_NEW_EVENT)!!
    val eventId: String? = savedStateHandle.get<String>(EditEventActivity.EXTRA_EVENT_ID)
    val eventTitle: String? = savedStateHandle.get<String>(EditEventActivity.EXTRA_EVENT_TITLE)
    val eventLimPart: Int? = savedStateHandle.get<Int>(EditEventActivity.EXTRA_EVENT_LIM_PART)
    val eventTime: IntArray? = savedStateHandle.get<IntArray>(EditEventActivity.EXTRA_EVENT_TIME)
    val eventDate: IntArray? = savedStateHandle.get<IntArray>(EditEventActivity.EXTRA_EVENT_DATE)
    private val eventLocation: DoubleArray? =
        savedStateHandle.get<DoubleArray>(EditEventActivity.EXTRA_EVENT_LOCATION)

    val title: MutableLiveData<String> = MutableLiveData(eventTitle)
    private val limPart: MutableLiveData<Int> = MutableLiveData(eventLimPart)
    val time: MutableLiveData<IntArray> = MutableLiveData(eventTime)
    val date: MutableLiveData<IntArray> = MutableLiveData(eventDate)
    private val location: MutableLiveData<DoubleArray> = MutableLiveData(eventLocation)

    /**
     * Set the title entered by the user
     * @param title: title entered by the user
     */
    fun setTitle(title: String) = this.title.postValue(title)

    /**
     * Set the limit of participants entered by the user
     * @param limPart: limit of participants entered by the user
     */
    fun setLimPart(limPart: Int) = this.limPart.postValue(limPart)

    /**
     * Set the time entered by the user
     * @param time: time entered by the user
     */
    fun setTime(time: IntArray) = this.time.postValue(time)

    /**
     * Set the date entered by the user
     * @param date: date entered by the user
     */
    fun setDate(date: IntArray) = this.date.postValue(date)

    /**
     * Set the location entered by the user
     * @param location: location entered by the user
     */
    fun setLocation(location: DoubleArray) = this.location.postValue(location)

    /**
     * Builds and submits the event to the database
     *
     * @throws IllegalStateException if the user is not connected, or if one of the fields is empty
     */
    fun submitEvent() {
        checkForErrors()

        val title = title.value!!
        val limPart = limPart.value!!
        val location = location.value!!
        val lat = location[0]
        val long = location[1]
        val uid = connectedUser.getUserId()!!
        val timeArray = time.value!!
        val dateArray = date.value!!
        val date = Date(dateArray[0], dateArray[1], dateArray[2])
        val time = Time(timeArray[0], timeArray[1])
        val duration = Duration()
        val period = Period(DateTime(date, time), duration)
        if (eventId == null) {
            val builder = Event.Builder()
                .name(title)
                .setLimitParticipants(limPart)
                .setCreator(uid)
                .setLat(lat)
                .setLong(long)
                .setPeriod(period)
            newEventBehavior(builder)
        } else {
            viewModelScope.launch {
                eventRepo.update(eventId) {
                    it.copy(
                        name = title,
                        limitParticipants = limPart,
                        lat = lat,
                        long = long,
                        period = period
                    )
                }.collect()
            }
        } // For CodeClimate ^^
    }

    /**
     * Check that there is no error before submitting
     */
    private fun checkForErrors() {
        // only connected users may add events
        if (!connectedUser.isLoggedIn()) {
            throw DisconnectedUserException()
        } else if (title.value.isNullOrEmpty()) {
            throw MissingInputException(NO_TITLE_MESSAGE)
        } else if (limPart.value == null) {
            throw MissingInputException(NO_LIM_PART_MESSAGE)
        } else if (time.value == null) {
            throw MissingInputException(NO_TIME_MESSAGE)
        } else if (date.value == null) {
            throw MissingInputException(NO_DATE_MESSAGE)
        } else if (location.value == null) {
            throw MissingInputException(NO_LOCATION_MESSAGE)
        } else if (limPart.value == 0) {
            throw IllegalStateException("The limit of participants cannot be 0.")
        }
    }

    private fun newEventBehavior(builder: Event.Builder) {
        try {
            val event = builder.build()
            viewModelScope.launch {
                eventRepo.add(event).collect()
            }
        } catch (e: IllegalStateException) {
            throw IllegalStateException("Failed to build the event (from ${e.message}")
        }
    }
}