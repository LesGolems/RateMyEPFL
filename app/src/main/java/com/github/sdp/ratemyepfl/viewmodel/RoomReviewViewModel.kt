package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.fragment.review.RoomReviewListFragment
import com.github.sdp.ratemyepfl.database.ReviewsRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class RoomReviewViewModel @Inject constructor(
    private val reviewsRepository: ReviewsRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val room: Classroom? = savedStateHandle.get<String>(RoomReviewListFragment.EXTRA_CLASSROOMS_JSON)?.let { Json.decodeFromString(it) }

    // Reviews of the classroom
    private val reviewsLiveData = MutableLiveData<List<Review?>>()
    
    init {
        updateReviewsList()
    }

    fun updateReviewsList() {
        viewModelScope.launch {
            reviewsLiveData.value = reviewsRepository.getByReviewableId(room?.id)
        }
    }

    fun getReviews(): LiveData<List<Review?>> {
        return reviewsLiveData
    }
}