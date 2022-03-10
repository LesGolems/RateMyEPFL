package com.github.sdp.ratemyepfl.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.ratemyepfl.review.ClassroomReview
import com.github.sdp.ratemyepfl.placeholder.DataSource
import java.time.LocalDate

class RoomReviewsListViewModel(
    private val dataSource: DataSource,
    private val id: String?
) : ViewModel() {

    // Reviews of the classroom
    private val reviewsLiveData = MutableLiveData(
        dataSource.getRoomForId(id)?.reviews
    )

    fun getReviews(): LiveData<List<ClassroomReview>?> {
        return reviewsLiveData
    }

    fun insertReview(roomGrade: String, roomComment: String) {
        val newReview = ClassroomReview(roomGrade.toInt(), roomComment, LocalDate.now())
        val currentList = reviewsLiveData.value
        if (currentList == null) { // No reviews yet
            reviewsLiveData.postValue(listOf(newReview))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, newReview)
            reviewsLiveData.postValue(updatedList)
        }
    }

    class RoomReviewsListViewModelFactory(
        private val dataSource: DataSource,
        private val id: String?
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RoomReviewsListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RoomReviewsListViewModel(
                    dataSource = dataSource,
                    id = id
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}