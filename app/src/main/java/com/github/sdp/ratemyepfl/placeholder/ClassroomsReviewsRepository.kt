package com.github.sdp.ratemyepfl.placeholder

import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.ClassroomReview
import com.github.sdp.ratemyepfl.model.review.ClassroomReview.Companion.toClassroomReview
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class ClassroomsReviewsRepository : Repository<ClassroomReview>() {
    companion object {
        const val COLLECTION = "classrooms_reviews"
    }

    override suspend fun add(value: ClassroomReview) {
        reviewsCollection().document(value.comment)
                           .set(value.toHashMap())
    }

    override suspend fun get(): List<ClassroomReview?> {
        return reviewsCollection().get().await().map {
                q -> q.toClassroomReview()
        }
    }

    suspend fun getByCourse(course: Course): Collection<ClassroomReview?> {
        return getBy("course", course.courseCode)
    }

    suspend fun getByRate(rate: Int): Collection<ClassroomReview?> {
        return getBy("rate", rate.toString())
    }

    suspend fun getByDate(date: LocalDate): Collection<ClassroomReview?> {
        return getBy("date", date.toString())
    }

    override suspend fun remove(value: ClassroomReview) {
        reviewsCollection().document(value.comment).delete()
    }

    private fun reviewsCollection(): CollectionReference {
        return db.collection(COLLECTION)
    }

    private suspend fun getBy(name: String, value: String): Collection<ClassroomReview?> {
        return reviewsCollection()
                    .whereEqualTo(name, value)
                    .get()
                    .await()
                    .map { q -> q.toClassroomReview() }
    }
}