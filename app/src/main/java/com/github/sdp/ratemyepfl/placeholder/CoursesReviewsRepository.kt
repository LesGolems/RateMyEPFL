package com.github.sdp.ratemyepfl.placeholder

import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.CourseReview
import com.github.sdp.ratemyepfl.model.review.CourseReview.Companion.toCourseReview
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class CoursesReviewsRepository : Repository<CourseReview>() {
    companion object {
        const val COLLECTION = "courses_reviews"
    }

    override suspend fun add(value: CourseReview) {
        reviewsCollection().document(value.title)
                           .set(value.toHashMap())
    }

    override suspend fun get(): List<CourseReview?> {
        return reviewsCollection().get().await().map {
                q -> q.toCourseReview()
        }
    }

    suspend fun getByCourse(course: Course): Collection<CourseReview?> {
        return getBy("course", course.courseCode)
    }

    suspend fun getByRating(rating: ReviewRating): Collection<CourseReview?> {
        return getBy("rating", rating.toString())
    }

    override suspend fun remove(value: CourseReview) {
        reviewsCollection().document(value.title).delete()
    }

    private fun reviewsCollection(): CollectionReference {
        return db.collection(COLLECTION)
    }

    private suspend fun getBy(name: String, value: String): Collection<CourseReview?> {
        return reviewsCollection()
                    .whereEqualTo(name, value)
                    .get()
                    .await()
                    .map { q -> q.toCourseReview() }
    }
}