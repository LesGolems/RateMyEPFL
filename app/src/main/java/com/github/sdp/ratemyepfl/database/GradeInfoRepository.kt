package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.GradeInfo
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction

interface GradeInfoRepository {

    suspend fun updateLikeRatio(item: Reviewable, reviewId: String, inc: Int): Task<Transaction>

    suspend fun addReview(
        item: Reviewable,
        reviewId: String,
        rating: ReviewRating
    ): Task<Transaction>

    suspend fun getGradeInfoById(itemId: String): GradeInfo?

}