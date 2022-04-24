package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.query.Query
import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.AVERAGE_GRADE_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.NUM_REVIEWS_FIELD_NAME
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ClassroomRepositoryImpl(val repository: ReviewableRepositoryImpl<Classroom>) :
    ClassroomRepository, ReviewableRepository<Classroom> by repository,
    Repository<Classroom> by repository {

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
        const val ROOM_KIND_FIELD_NAME = "roomKind"
        const val NAME_FIELD_NAME = "name"

        fun DocumentSnapshot.toClassroom(): Classroom? {
            val builder = Classroom.Builder()
                .setName(getString(NAME_FIELD_NAME))
                .setRoomKind(getString(ROOM_KIND_FIELD_NAME))
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
        return repository.take(Query.DEFAULT_QUERY_LIMIT.toLong())
            .mapNotNull { obj -> obj.toClassroom() }
    }

    override suspend fun getRoomById(id: String): Classroom? = repository.getById(id).toClassroom()

    override suspend fun updateClassroomRating(id: String, rating: ReviewRating) =
        repository
            .updateRating(id, rating)
    
    override fun search(pattern: String): QueryResult<List<Classroom>> =
        repository.search(pattern, NAME_FIELD_NAME)


}