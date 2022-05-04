package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.GradeInfoRepository
import com.github.sdp.ratemyepfl.model.GradeInfo
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import org.mockito.Mockito
import javax.inject.Inject

class FakeGradeInfoRepository @Inject constructor() : GradeInfoRepository {
    override fun update(id: String, transform: (GradeInfo) -> GradeInfo): Task<Transaction> {
        return Mockito.mock(Task::class.java) as Task<Transaction>
    }

    override suspend fun getGradeAndNumReviewForId(id: String): Pair<Double, Int> = Pair(0.0, 0)

    override fun updateLikeRatio(id: String, rid: String, inc: Int): Task<Transaction> {
        return Mockito.mock(Task::class.java) as Task<Transaction>
    }

    override suspend fun addReview(
        id: String,
        rid: String,
        rating: ReviewRating
    ): Task<Transaction> {
        return Mockito.mock(Task::class.java) as Task<Transaction>
    }
}