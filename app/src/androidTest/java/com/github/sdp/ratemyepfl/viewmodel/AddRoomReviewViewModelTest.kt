package com.github.sdp.ratemyepfl.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class AddRoomReviewViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun setRoomNameWorksForAValidRoomName() {
        val viewModel = AddRoomReviewViewModel()
        val roomName = "BC-229"
        viewModel.setRoomName(roomName)

        assertEquals(roomName, viewModel.getRoomName())
    }

    @Test
    fun setCommentWorksForAValidComment() {
        val comment = "my comment"
        val viewModel: AddRoomReviewViewModel = AddRoomReviewViewModel()
        viewModel.setComment(comment)

        assertEquals(comment, viewModel.getComment())
    }

    @Test
    fun setRatingWorksForValidRating() {
        val rating = ReviewRating.AVERAGE
        val viewModel = AddRoomReviewViewModel()
        viewModel.setRating(rating)

        assertEquals(rating, viewModel.getRating())
    }
}