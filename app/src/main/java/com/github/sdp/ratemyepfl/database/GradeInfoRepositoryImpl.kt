package com.github.sdp.ratemyepfl.database

import android.util.Log
import com.github.sdp.ratemyepfl.model.GradeInfo
import com.github.sdp.ratemyepfl.model.ReviewInfo
import com.github.sdp.ratemyepfl.model.ReviewInfo.Companion.DEFAULT_REVIEW_INFO
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.getField
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GradeInfoRepositoryImpl(val repository: RepositoryImpl<GradeInfo>) : GradeInfoRepository,
    Repository<GradeInfo> by repository {

    @Inject
    constructor(db: FirebaseFirestore) : this(RepositoryImpl<GradeInfo>(
        db,
        GradeInfoRepositoryImpl.GRADE_INFO_COLLECTION_PATH
    ) {
        it.toGradeInfo()
    })

    companion object {
        const val NUM_USERS = 6.0
        const val GRADE_INFO_COLLECTION_PATH = "grades_info"
        const val ITEM_ID_FIELD = "itemId"
        const val REVIEWS_INFO_FIELD = "reviewsData"
        const val CURRENT_GRADE_FIELD = "currentGrade"
        const val NUM_REVIEWS_FIELD = "numReviews"

        fun DocumentSnapshot.toGradeInfo(): GradeInfo? {
            val type = object : TypeToken<Map<String, ReviewInfo>>() {}.type
            val reviewsData = getString(REVIEWS_INFO_FIELD)?.let {
                Gson().fromJson<Map<String, ReviewInfo>>(it, type)
            }
            return try {
                GradeInfo.Builder(
                    getString(ITEM_ID_FIELD),
                    reviewsData,
                    getDouble(CURRENT_GRADE_FIELD),
                    getField<Int>(NUM_REVIEWS_FIELD)
                ).build()
            } catch (e: IllegalStateException) {
                null
            }
        }
    }

    /**
     * Compute the overall grade the number of reviews from all the grade info
     * (FOR NOW BASIC AVERAGE)
     */
    private fun computeGrade(reviewsData: Map<String, ReviewInfo>): Double {
        var totalGrade = 0.0
        var total = 0.0
        for (ri in reviewsData.values) {
            val w = reviewWeight(ri.likeRatio)
            totalGrade += ri.reviewGrade * w
            total += w
        }
        return totalGrade / total
    }


    /**
     * Computes the weight of a review, the weight goes down as the review like ratio goes negative.
     * The weight is based on the approximate number of users of the app
     */
    private fun reviewWeight(likeRatio: Int): Double {
        return if (likeRatio >= 0) 1.0
        else if(likeRatio < -NUM_USERS) 0.0
        else 1 - (-likeRatio) / NUM_USERS
    }

    override fun updateLikeRatio(itemId: String, reviewId: String, inc: Int): Task<Transaction> =
        repository.update(itemId) {
            val info: ReviewInfo = it.reviewsData.getOrDefault(reviewId, DEFAULT_REVIEW_INFO)
            val newData = it.reviewsData.plus(
                Pair(
                    reviewId,
                    ReviewInfo(
                        info.reviewGrade,
                        info.likeRatio + inc
                    )
                )
            )
            it.copy(
                reviewsData = newData,
                currentGrade = computeGrade(newData),
                numReviews = newData.size
            )
        }

    override suspend fun addReview(
        itemId: String,
        reviewId: String,
        rating: ReviewRating
    ): Task<Transaction> {
        if (getGradeInfoById(itemId) == null) {
            repository.add(GradeInfo(itemId)).await()
        }

        return repository.update(itemId) {
            val newData = it.reviewsData.plus(
                Pair(
                    reviewId,
                    ReviewInfo(rating.toValue(), 0)
                )
            )
            it.copy(
                reviewsData = newData,
                currentGrade = computeGrade(newData),
                numReviews = newData.size
            )
        }
    }

    override suspend fun getGradeInfoById(itemId: String): GradeInfo? = repository
        .getById(itemId)
        .toGradeInfo()
}