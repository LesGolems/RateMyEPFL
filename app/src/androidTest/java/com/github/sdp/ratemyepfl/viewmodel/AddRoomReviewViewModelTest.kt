package com.github.sdp.ratemyepfl.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.sdp.ratemyepfl.database.ClassroomsRepositoryInterface
import com.github.sdp.ratemyepfl.database.ClassroomsReviewsRepositoryInterface
import com.github.sdp.ratemyepfl.database.FakeClassroomsRepository
import com.github.sdp.ratemyepfl.database.FakeClassroomsReviewsRepository
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class AddRoomReviewViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun setRoomNameWorksForAValidRoomName() {
        val database = FakeClassroomsReviewsRepository()
        val viewModel = AddRoomReviewViewModel(database)
        val roomName = "BC-229"
        viewModel.setRoomName(roomName)

        assertEquals(roomName, viewModel.getRoomName())
    }

    @Test
    fun setCommentWorksForAValidComment() {
        val comment = "my comment"
        val database = FakeClassroomsReviewsRepository()
        val viewModel = AddRoomReviewViewModel(database)
        viewModel.setComment(comment)

        assertEquals(comment, viewModel.getComment())
    }

    @Test
    fun setRatingWorksForValidRating() {
        val rating = ReviewRating.AVERAGE
        val database = FakeClassroomsReviewsRepository()
        val viewModel = AddRoomReviewViewModel(database)
        viewModel.setRating(rating)

        assertEquals(rating, viewModel.getRating())
    }
}