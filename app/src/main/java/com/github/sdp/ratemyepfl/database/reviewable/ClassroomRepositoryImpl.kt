package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.DatabaseException
import com.github.sdp.ratemyepfl.database.LoaderRepository
import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.query.OrderDirection
import com.github.sdp.ratemyepfl.database.query.OrderedQuery
import com.github.sdp.ratemyepfl.database.query.Query
import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.AVERAGE_GRADE_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.NUM_REVIEWS_FIELD_NAME
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClassroomRepositoryImpl private constructor(private val repository: ReviewableRepositoryImpl<Classroom>) :
    ClassroomRepository, ReviewableRepository<Classroom> by repository,
    LoaderRepository<Classroom> by repository {

    private val queryLoadClassrooms = repository
        .query()
        .orderBy(AVERAGE_GRADE_FIELD_NAME, OrderDirection.DESCENDING)
        .orderBy(ROOM_NAME_FIELD_NAME)

    @Inject
    constructor(db: FirebaseFirestore) : this(
        ReviewableRepositoryImpl(
            db,
            CLASSROOM_COLLECTION_PATH,
            ROOM_NAME_FIELD_NAME,
            OFFLINE_CLASSROOMS
        ) { documentSnapshot ->
            documentSnapshot.toClassroom()
        })

    companion object {
        const val CLASSROOM_COLLECTION_PATH = "rooms"
        const val ROOM_KIND_FIELD_NAME = "roomKind"
        const val ROOM_NAME_FIELD_NAME = "name"

        private val OFFLINE_CLASSROOMS: List<Classroom> = listOf(
            Classroom("BC233", 0, 0.0),
            Classroom("CE 1 1", 0, 0.0),
            Classroom("CE 1 4", 0, 0.0),
            Classroom("CM 1 1", 0, 0.0),
            Classroom("CM 1 3", 0, 0.0),
            Classroom("CM 1 4", 0, 0.0),
            Classroom("CM 1 5", 0, 0.0),
            Classroom("ELA 1", 0, 0.0),
        )

        fun DocumentSnapshot.toClassroom(): Classroom? = try {
            val builder = Classroom.Builder()
                .setName(getString(ROOM_NAME_FIELD_NAME))
                .setRoomKind(getString(ROOM_KIND_FIELD_NAME))
                .setNumReviews(getField<Int>(NUM_REVIEWS_FIELD_NAME))
                .setAverageGrade(getDouble(AVERAGE_GRADE_FIELD_NAME))

            builder.build()
        } catch (e: IllegalStateException) {
            null
        } catch (e: Exception) {
            throw DatabaseException("Failed to convert the fetched document in Classroom")
        }

    }

    override suspend fun getClassrooms(): List<Classroom> {
        return repository.take(Query.DEFAULT_QUERY_LIMIT.toLong())
            .mapNotNull { obj -> obj.toClassroom() }
    }

    override suspend fun getRoomById(id: String): Classroom? = repository.getById(id).toClassroom()

    override suspend fun updateClassroomRating(id: String, rating: ReviewRating) {
        repository
            .update(id) { classroom ->
                val (updatedNumReviews, updatedAverageGrade) = ReviewableRepositoryImpl.computeUpdatedRating(
                    classroom,
                    rating
                )
                classroom.copy(numReviews = updatedNumReviews, averageGrade = updatedAverageGrade)
            }.await()
    }

    override suspend fun load(number: UInt): QueryResult<List<Classroom>> =
        repository
            .load(queryLoadClassrooms, number)
            .withDefault<FirebaseNetworkException>(OFFLINE_CLASSROOMS)


    override fun loaded(): List<Classroom>? =
        repository.loaded(queryLoadClassrooms)

    override fun search(prefix: String): QueryResult<List<Classroom>> =
        repository.search(ROOM_NAME_FIELD_NAME, prefix)


}