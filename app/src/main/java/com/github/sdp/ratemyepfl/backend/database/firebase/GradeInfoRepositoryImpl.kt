package com.github.sdp.ratemyepfl.backend.database.firebase

import com.github.sdp.ratemyepfl.backend.database.GradeInfoRepository
import com.github.sdp.ratemyepfl.backend.database.Repository
import com.github.sdp.ratemyepfl.backend.database.firebase.RepositoryImpl.Companion.toItem
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.ClassroomRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.CourseRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.EventRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.model.GradeInfo
import com.github.sdp.ratemyepfl.model.ReviewInfo
import com.github.sdp.ratemyepfl.model.items.*
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
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
            GRADE_INFO_COLLECTION_PATH
        ) {
            it.toGradeInfo()
        }, courseRepository, classroomRepository, restaurantRepository, eventRepository
    )

    companion object {
        const val NUM_USERS = 6.0
        const val GRADE_INFO_COLLECTION_PATH = "grades_info"
        const val ITEM_ID_FIELD = "itemId"
        const val REVIEWS_INFO_FIELD = "reviewsData"

        fun DocumentSnapshot.toGradeInfo(): GradeInfo? = toItem()
    }

    override suspend fun updateLikeRatio(
        item: Reviewable,
        reviewId: String,
        inc: Int
    ) {
        val computedGrade = repository.update(item.getId()) {
            val info: ReviewInfo = it.reviewsData.getOrDefault(reviewId, ReviewInfo(0, 0))
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
                reviewsData = newData
            )
        }
            .last()
            .computeGrade()

        updateItem(item, computedGrade, 0)
    }

    override suspend fun removeReview(
        item: Reviewable,
        reviewId: String
    ) {
        val computedGrade = repository.update(item.getId()) {
            val newData = it.reviewsData.minus(reviewId)
            it.copy(
                reviewsData = newData
            )
        }.last()
            .computeGrade()

        updateItem(item, computedGrade, -1)
    }

    override suspend fun addReview(
        item: Reviewable,
        reviewId: String,
        rating: ReviewRating
    ) {
        if (getGradeInfoById(item.getId()) == null) {
            repository.add(GradeInfo(item.getId())).collect()
        }

        val computedGrade = repository.update(item.getId()) {
            val newData = it.reviewsData.plus(
                Pair(
                    reviewId,
                    ReviewInfo(rating.toValue(), 0)
                )
            )
            it.copy(
                reviewsData = newData
            )
        }.map { it.computeGrade() }
            .last()

        updateItem(item, computedGrade, 1)
    }

    override suspend fun getGradeInfoById(itemId: String): GradeInfo = repository
        .getById(itemId).last()

    private suspend fun updateItem(item: Reviewable, grade: Double, incNumReviews: Int) =
        when (item) {
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
        }.collect()
}