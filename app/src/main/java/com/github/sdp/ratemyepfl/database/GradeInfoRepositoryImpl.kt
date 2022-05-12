package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.EventRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.model.GradeInfo
import com.github.sdp.ratemyepfl.model.ReviewInfo
import com.github.sdp.ratemyepfl.model.ReviewInfo.Companion.DEFAULT_REVIEW_INFO
import com.github.sdp.ratemyepfl.model.items.*
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

class GradeInfoRepositoryImpl private constructor(
    private val repository: RepositoryImpl<GradeInfo>,
    private val courseRepository: CourseRepositoryImpl,
    private val classroomRepository: ClassroomRepositoryImpl,
    private val restaurantRepository: RestaurantRepositoryImpl,
    private val eventRepository: EventRepositoryImpl
) : GradeInfoRepository,
    Repository<GradeInfo> by repository {

    @Inject
    constructor(
        db: FirebaseFirestore, courseRepository: CourseRepositoryImpl,
        classroomRepository: ClassroomRepositoryImpl,
        restaurantRepository: RestaurantRepositoryImpl,
        eventRepository: EventRepositoryImpl
    ) : this(
        RepositoryImpl<GradeInfo>(
            db,
            GradeInfoRepositoryImpl.GRADE_INFO_COLLECTION_PATH
        ) {
            it.toGradeInfo()
        }, courseRepository, classroomRepository, restaurantRepository, eventRepository
    )

    companion object {
        const val NUM_USERS = 6.0
        const val GRADE_INFO_COLLECTION_PATH = "grades_info"
        const val ITEM_ID_FIELD = "itemId"
        const val REVIEWS_INFO_FIELD = "reviewsData"

        fun DocumentSnapshot.toGradeInfo(): GradeInfo? {
            val type = object : TypeToken<Map<String, ReviewInfo>>() {}.type
            val reviewsData = getString(REVIEWS_INFO_FIELD)?.let {
                Gson().fromJson<Map<String, ReviewInfo>>(it, type)
            }
            return try {
                GradeInfo.Builder(
                    getString(ITEM_ID_FIELD),
                    reviewsData
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
        else if (likeRatio < -NUM_USERS) 0.0
        else 1 - (-likeRatio) / NUM_USERS
    }

    override suspend fun updateLikeRatio(item: Reviewable, reviewId: String, inc: Int): Task<Transaction> {
        var computedGrade = 0.0
        repository.update(item.getId()) {
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
            computedGrade = computeGrade(newData)
            it.copy(
                reviewsData = newData
            )
        }.await()

        return updateItem(item, computedGrade, 0)
    }

    override suspend fun addReview(
        item: Reviewable,
        reviewId: String,
        rating: ReviewRating
    ): Task<Transaction> {
        if (getGradeInfoById(item.getId()) == null) {
            repository.add(GradeInfo(item.getId())).await()
        }

        var computedGrade = 0.0

        repository.update(item.getId()) {
            val newData = it.reviewsData.plus(
                Pair(
                    reviewId,
                    ReviewInfo(rating.toValue(), 0)
                )
            )
            computedGrade = computeGrade(newData)
            it.copy(
                reviewsData = newData
            )
        }.await()

        return updateItem(item, computedGrade, 1)
    }

    override suspend fun getGradeInfoById(itemId: String): GradeInfo? = repository
        .getById(itemId)
        .toGradeInfo()

    private fun updateItem(item: Reviewable, grade: Double, incNumReviews : Int): Task<Transaction> = when (item) {
        is Classroom -> classroomRepository.update(item.getId()) {
            it.copy(grade = grade, numReviews = it.numReviews + incNumReviews)
        }
        is Course -> courseRepository.update(item.getId()) {
            it.copy(grade = grade, numReviews = it.numReviews + incNumReviews)
        }
        is Event -> eventRepository.update(item.getId()) {
            it.copy(grade = grade, numReviews = it.numReviews + incNumReviews)
        }
        is Restaurant -> restaurantRepository.update(item.getId()) {
            it.copy(grade = grade, numReviews = it.numReviews + incNumReviews)
        }
        else -> throw Exception("")
    }
}