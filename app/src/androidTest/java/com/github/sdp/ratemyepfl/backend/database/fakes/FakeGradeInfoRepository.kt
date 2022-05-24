package com.github.sdp.ratemyepfl.backend.database.fakes

import com.github.sdp.ratemyepfl.backend.database.GradeInfoRepository
import com.github.sdp.ratemyepfl.model.GradeInfo
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import org.mockito.Mockito
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class FakeGradeInfoRepository @Inject constructor() : GradeInfoRepository {

    companion object {
        private val NO_REVIEW = GradeInfo("id", mapOf())
        var gradeById = NO_REVIEW
    }

    override suspend fun getGradeInfoById(itemId: String): GradeInfo = gradeById


    override suspend fun updateLikeRatio(item: Reviewable, reviewId: String, inc: Int): Task<Unit> {
        return Mockito.mock(Task::class.java) as Task<Unit>
    }

    override suspend fun addReview(
        item: Reviewable,
        reviewId: String,
        rating: ReviewRating
    ): Task<Unit> {
        return Mockito.mock(Task::class.java) as Task<Unit>
    }

    override suspend fun removeReview(item: Reviewable, reviewId: String): Task<Unit> {
        return Mockito.mock(Task::class.java) as Task<Unit>
    }
}