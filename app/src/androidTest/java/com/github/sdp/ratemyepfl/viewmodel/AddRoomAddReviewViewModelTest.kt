package com.github.sdp.ratemyepfl.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.sdp.ratemyepfl.database.FakeReviewsRepository
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class AddRoomAddReviewViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun setRoomNameWorksForAValidRoomName() {
        val database = FakeReviewsRepository()
        val viewModel = AddRoomReviewViewModel(database)
        val roomName = "BC-229"
        viewModel.setRoomName(roomName)

        assertEquals(roomName, viewModel.getRoomName())
    }

    @Test
    fun setCommentWorksForAValidComment() {
        val comment = "my comment"
        val database = FakeReviewsRepository()
        val viewModel = AddRoomReviewViewModel(database)
        viewModel.setComment(comment)

        assertEquals(comment, viewModel.getComment())
    }

    @Test
    fun setRatingWorksForValidRating() {
        val rating = ReviewRating.AVERAGE
        val database = FakeReviewsRepository()
        val viewModel = AddRoomReviewViewModel(database)
        viewModel.setRating(rating)

        assertEquals(rating, viewModel.getRating())
    }
}