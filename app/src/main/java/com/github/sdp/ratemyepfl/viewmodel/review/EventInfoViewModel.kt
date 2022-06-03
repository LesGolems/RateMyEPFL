package com.github.sdp.ratemyepfl.viewmodel.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.backend.database.UserRepository
import com.github.sdp.ratemyepfl.backend.database.reviewable.EventRepository
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.EventWithAuthor
import com.github.sdp.ratemyepfl.ui.activity.ReviewActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventInfoViewModel @Inject constructor(
    private val eventRepo: EventRepository,
    private val userRepo: UserRepository,
    private val imageStorage: Storage<ImageFile>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED_ID)!!

    val eventWithAuthor = MutableLiveData<EventWithAuthor>()

    init {
        updateEvent()
    }

    fun updateEvent() {
        viewModelScope.launch {
            eventWithAuthor.postValue(
                eventRepo.getEventById(id).let { event ->
                    EventWithAuthor(
                        event,
                        event.creator.let { userRepo.getUserByUid(it) },
                        event.creator.let { imageStorage.get(it).lastOrNull() })
                }
            )
        }
    }

}