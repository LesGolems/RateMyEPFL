package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.model.review.Post

open class AddPostViewModel<T : Post> : ViewModel() {

    companion object {
        const val EMPTY_TITLE_MESSAGE: String = "Please enter a title"
        const val EMPTY_COMMENT_MESSAGE: String = "Please enter a comment"
    }

    val title: MutableLiveData<String> = MutableLiveData(null)
    val comment: MutableLiveData<String> = MutableLiveData(null)
    var anonymous: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * Set the title entered by the user
     * @param title: title entered by the user
     */
    fun setTitle(title: String?) = this.title.postValue(title)

    /**
     * Set the comment entered by the user
     * @param comment: comment entered by the user
     */
    fun setComment(comment: String?) = this.comment.postValue(comment)

    /**
     * Set the anonymous option
     * @param anonymous: whether or not the user wants to post as anonymous
     */
    fun setAnonymous(anonymous: Boolean) {
        this.anonymous.postValue(anonymous)
    }

}