package com.github.sdp.ratemyepfl.placeholder

import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.ClassroomReview
import com.github.sdp.ratemyepfl.model.review.ClassroomReview.Companion.toClassroomReview
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

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
        return reviewsCollection()
            .whereEqualTo("course", course.courseCode)
            .get()
            .await()
            .map { q -> q.toClassroomReview() }
    }

    override suspend fun remove(value: ClassroomReview) {
        reviewsCollection().document(value.comment).delete()
    }

    private fun reviewsCollection(): CollectionReference {
        return db.collection(COLLECTION)
    }
}