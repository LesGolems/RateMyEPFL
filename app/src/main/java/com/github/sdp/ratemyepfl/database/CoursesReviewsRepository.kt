package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.CourseReview
import com.github.sdp.ratemyepfl.model.review.CourseReview.Companion.toCourseReview
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

class CoursesReviewsRepository @Inject constructor() : CoursesReviewsRepositoryInterface{
    private val db = FirebaseFirestore.getInstance()

    companion object {
        const val COLLECTION = "courses_reviews"
    }

    override fun add(value: CourseReview) {
        reviewsCollection().document(value.title)
                           .set(value.toHashMap())
    }

    override suspend fun get(): List<CourseReview?> {
        return reviewsCollection().get().await().map {
                q -> q.toCourseReview()
        }
    }

    suspend fun getByCourse(course: Course): List<CourseReview?> {
        return getBy("course", course.courseCode)
    }

    suspend fun getByRating(rating: ReviewRating): List<CourseReview?> {
        return getBy("rating", rating.toString())
    }

    suspend fun getByDate(date: LocalDate): List<CourseReview?> {
        return getBy("date", date.toString())
    }

    fun remove(value: CourseReview) {
        reviewsCollection().document(value.title).delete()
    }

    private fun reviewsCollection(): CollectionReference {
        return db.collection(COLLECTION)
    }

    private suspend fun getBy(name: String, value: String): List<CourseReview?> {
        return reviewsCollection()
                    .whereEqualTo(name, value)
                    .get()
                    .await()
                    .map { q -> q.toCourseReview() }
    }
}