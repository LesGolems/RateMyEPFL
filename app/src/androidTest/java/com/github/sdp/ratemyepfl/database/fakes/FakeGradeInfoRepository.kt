package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.GradeInfoRepository
import com.github.sdp.ratemyepfl.model.GradeInfo
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import org.mockito.Mockito
import javax.inject.Inject

class FakeGradeInfoRepository @Inject constructor() : GradeInfoRepository {

    companion object{
        val NO_REVIEW = GradeInfo("id", mapOf(), 0.0, 0)
        var gradeById = NO_REVIEW
    }

    override suspend fun getGradeInfoById(itemId: String): GradeInfo? = gradeById

    override fun updateLikeRatio(itemId: String, reviewId: String, inc: Int): Task<Transaction> {
        return Mockito.mock(Task::class.java) as Task<Transaction>
    }

    override suspend fun addReview(
        item: Reviewable,
        reviewId: String,
        rating: ReviewRating
    ): Task<Transaction> {
        return Mockito.mock(Task::class.java) as Task<Transaction>
    }
}