package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.model.review.ClassroomReview
import com.github.sdp.ratemyepfl.placeholder.DataSource
import java.time.LocalDate

class RoomReviewsListViewModel (private val dataSource: DataSource, private val id: String?) : ViewModel() {

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

}