package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.GradeInfo
import com.github.sdp.ratemyepfl.model.ReviewInfo
import com.github.sdp.ratemyepfl.model.ReviewInfo.Companion.DEFAULT_REVIEW_INFO
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GradeInfoRepositoryImpl (val repository: RepositoryImpl<GradeInfo>) : GradeInfoRepository, Repository<GradeInfo> by repository {

    @Inject
    constructor(db: FirebaseFirestore) : this(RepositoryImpl<GradeInfo>(db,
        GradeInfoRepositoryImpl.GRADE_INFO_COLLECTION_PATH
    ) {
        it.toGradeInfo()
    })

    companion object {
        const val GRADE_INFO_COLLECTION_PATH = "grades_info"
        const val ITEM_ID_FIELD = "itemId"
        const val REVIEWS_INFO_FIELD = "reviewsData"

        fun DocumentSnapshot.toGradeInfo(): GradeInfo? = try {
            val type = object: TypeToken<Map<String, ReviewInfo>>(){}.type
            val reviewsData = getString(REVIEWS_INFO_FIELD)?.let {
                Gson().fromJson<Map<String, ReviewInfo>>(it, type)
            }
            if (reviewsData != null) {
                GradeInfo(id, reviewsData)
            }
            else{
                null
            }
        } catch (e: IllegalStateException) {
            null
        }
    }

    override fun updateLikeRatio(id: String, rid: String, inc : Int): Task<Transaction> =
        repository.update(id) {
            val info: ReviewInfo = it.reviewsData.getOrDefault(rid, DEFAULT_REVIEW_INFO)
            it.copy(
                reviewsData = it.reviewsData.plus(Pair(rid,
                    ReviewInfo(
                        info.reviewGrade,
                        info.likeRatio + inc
                    )
                ))
            )
        }

    override suspend fun addReview(id: String, rid: String, rating: ReviewRating): Task<Transaction> {
        if (getGradeInfoById(id) == null) {
            repository.add(GradeInfo(id)).await()
        }

        return repository.update(id) {
            it.copy(
                reviewsData = it.reviewsData.plus(
                    Pair(
                        rid,
                        ReviewInfo(rating.toValue(), 0)
                    )
                )
            )
        }
    }

    override suspend fun getGradeAndNumReviewForId(id: String): Pair<Double, Int> {
        return getById(id).toGradeInfo()?.computeGrade() ?: Pair(0.0, 0)
    }

    suspend fun getGradeInfoById(id: String): GradeInfo? = repository
        .getById(id)
        .toGradeInfo()
}