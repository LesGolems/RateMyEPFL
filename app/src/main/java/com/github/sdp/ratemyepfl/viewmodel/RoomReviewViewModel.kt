package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.activity.RoomReviewActivity
import com.github.sdp.ratemyepfl.database.ItemsRepository
import com.github.sdp.ratemyepfl.database.ItemsRepositoryInterface
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
    private val itemsRepository: ItemsRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val id: String? = savedStateHandle.get<String>(RoomReviewActivity.EXTRA_CLASSROOMS_ID)

    // Classroom
    private val room = MutableLiveData<Classroom?>()
    // Reviews of the classroom
    private val reviewsLiveData = MutableLiveData<List<Review?>>()

    
    init {
        updateRoom()
        updateReviewsList()
    }

    fun updateRoom() {
        viewModelScope.launch {
            room.value = itemsRepository.getByIdClassrooms(id!!)
        }
    }

    fun updateReviewsList() {
        viewModelScope.launch {
            reviewsLiveData.value = reviewsRepository.getByReviewableId(id)
        }
    }

    fun getReviews(): LiveData<List<Review?>> {
        return reviewsLiveData
    }

    fun getRoom(): LiveData<Classroom?> {
        return room
    }
}