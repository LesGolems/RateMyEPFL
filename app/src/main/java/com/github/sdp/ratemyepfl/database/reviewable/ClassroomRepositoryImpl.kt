package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.QueryResult
import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.AVERAGE_GRADE_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.NUM_REVIEWS_FIELD_NAME
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ClassroomRepositoryImpl(val repository: ReviewableRepositoryImpl<Classroom>) :
    ClassroomRepository, ReviewableRepository<Classroom> by repository {

    @Inject
    constructor(db: FirebaseFirestore) : this(
        ReviewableRepositoryImpl(
            db,
            CLASSROOM_COLLECTION_PATH
        ) { documentSnapshot ->
            documentSnapshot.toClassroom()
        })

    companion object {
        const val CLASSROOM_COLLECTION_PATH = "rooms"
        const val ROOM_KIND_FIELD = "roomKind"

        fun DocumentSnapshot.toClassroom(): Classroom? {
            val builder = Classroom.Builder()
                .setId(id)
                .setRoomKind(getString(ROOM_KIND_FIELD))
                .setNumReviews(getString(NUM_REVIEWS_FIELD_NAME)?.toInt())
                .setAverageGrade(getString(AVERAGE_GRADE_FIELD_NAME)?.toDouble())

            return try {
                builder.build()
            } catch (e: IllegalStateException) {
                null
            }
        }
    }

    override suspend fun getClassrooms(): List<Classroom> {
        return repository.take(Repository.DEFAULT_QUERY_LIMIT)
            .mapNotNull { obj -> obj.toClassroom() }
    }

    override suspend fun getRoomById(id: String): Classroom? = repository.getById(id).toClassroom()

    override suspend fun updateClassroomRating(id: String, rating: ReviewRating) =
        repository
            .updateRating(id, rating)

    fun add(room: Classroom) {
        repository
            .collection
            .document(room.id)
            .set(room.toHashMap())
    }

    private val fromReviewable: (Reviewable) -> Classroom = { it as Classroom }

    override fun search(pattern: String): QueryResult<List<Classroom>> =
        repository.search(pattern)


}