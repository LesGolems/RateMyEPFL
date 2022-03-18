package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.database.ReviewsRepositoryInterface
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddRoomReviewViewModel @Inject constructor(val database: ReviewsRepositoryInterface) : ViewModel() {

    private var roomName: String? = null
    val comment: MutableLiveData<String> = MutableLiveData(null)
    val rating: MutableLiveData<ReviewRating> = MutableLiveData(null)

    fun getRoomName() = roomName
    fun setRoomName(name: String?) {
        roomName = name
    }

    fun getRating() = this.rating.value

    fun setRating(rating: ReviewRating?) {
        this.rating.postValue(rating)
    }

    fun getComment() = this.comment.value

    fun setComment(comment: String?) {
        this.comment.postValue(comment)
    }

    fun submitReview(): Boolean {
        val rating = rating.value
        val comment = comment.value

        if (rating != null && comment != null) {
            val review = ClassroomReview(rating, "", comment, LocalDate.now())
            database.add(review)
            return true
        }
        return false
    }

}