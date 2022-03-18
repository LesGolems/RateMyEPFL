package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.activity.classrooms.ClassroomsListActivity
import com.github.sdp.ratemyepfl.database.ClassroomsReviewsRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RoomReviewsListViewModel @Inject constructor(
    private val reviewsRepository: ClassroomsReviewsRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val id: String? = savedStateHandle.get<String>(ClassroomsListActivity.EXTRA_ROOM_ID)

    // Reviews of the classroom
    private val reviewsLiveData = MutableLiveData<List<ClassroomReview?>>()

    init {
        updateReviewsList()
    }

    private fun updateReviewsList() {
        viewModelScope.launch {
            reviewsLiveData.value = reviewsRepository.getByClassroom(id)
        }
    }

    fun getReviews(): LiveData<List<ClassroomReview?>> {
        return reviewsLiveData
    }

    fun insertReview(roomGrade: Int, roomComment: String) {
        val newReview = ClassroomReview(roomGrade, roomComment, LocalDate.now())
        val currentList = reviewsLiveData.value
        if (currentList == null) { // No reviews yet
            reviewsLiveData.postValue(listOf(newReview))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, newReview)
            reviewsLiveData.postValue(updatedList)
        }
    }

}