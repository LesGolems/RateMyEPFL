package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddRoomReviewViewModel @Inject constructor() : ViewModel() {

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

}