package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.activity.classrooms.ClassroomsListActivity
import com.github.sdp.ratemyepfl.model.review.ClassroomReview
import com.github.sdp.ratemyepfl.placeholder.DataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RoomReviewsListViewModel @Inject constructor(
    private val dataSource: DataSource,
    // To retrieve room information
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val roomId: String? = savedStateHandle.get<String>(ClassroomsListActivity.EXTRA_ROOM_ID)

    // Reviews of the classroom
    private val reviewsLiveData = MutableLiveData(
        dataSource.getRoomForId(roomId)?.reviews
    )

    fun getReviews(): LiveData<List<ClassroomReview>?> {
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