package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.GradeInfo
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction

interface GradeInfoRepository {

    fun update(id: String, transform: (GradeInfo) -> GradeInfo): Task<Transaction>

    /**
     * Returns the overall grade and the number of reviews for reviewable with [id]
     */
    suspend fun getGradeAndNumReviewForId(id: String): Pair<Double, Int>

    fun updateLikeRatio(id: String, rid: String, inc : Int): Task<Transaction>

    suspend fun addReview(id: String, rid: String, rating: ReviewRating): Task<Transaction>

}