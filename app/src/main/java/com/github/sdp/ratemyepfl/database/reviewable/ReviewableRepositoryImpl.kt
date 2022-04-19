package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.*
import com.github.sdp.ratemyepfl.database.SearchableRepository.Companion.LIMIT_QUERY_SEARCH
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

private const val SUFFIX_MATCHER = "\uf8ff"

/**
 * Decorator for Repository that defines reviewable related operations
 */
class ReviewableRepositoryImpl<T: Reviewable>(
    val repository: LoaderRepositoryImpl<T>,
) : ReviewableRepository<T>, Repository by repository, LoaderRepository<T> by repository {

    constructor(
        database: FirebaseFirestore,
        collectionPath: String,
        transform: (DocumentSnapshot) -> T?
    ) : this(
        LoaderRepositoryImpl(RepositoryImpl(database, collectionPath), transform)
    )

    val collection = repository.collection()
    val database = repository.database()

    companion object {
        const val NUM_REVIEWS_FIELD_NAME = "numReviews"
        const val AVERAGE_GRADE_FIELD_NAME = "averageGrade"
    }

    /**
     * Updates the rating of a reviewable item using a transaction for concurrency
     *
     *  @param id : id of the reviewed item
     *  @param rating: rating of the review being added
     */
    suspend fun updateRating(id: String, rating: ReviewRating) {
        val docRef = collection
            .document(id)
        database
            .runTransaction {
                val snapshot = it.get(docRef)
                val numReviews = snapshot.getString(NUM_REVIEWS_FIELD_NAME)?.toInt()
                val averageGrade = snapshot.getString(AVERAGE_GRADE_FIELD_NAME)?.toDouble()
                if (numReviews != null && averageGrade != null) {
                    val newNumReviews = numReviews + 1
                    val newAverageGrade =
                        averageGrade + (rating.toValue() - averageGrade) / newNumReviews
                    it.update(
                        docRef, "numReviews", newNumReviews.toString(),
                        "averageGrade", newAverageGrade.toString()
                    )
                }
            }.await()
    }

    private val queryMostRated = collection
        .orderBy(NUM_REVIEWS_FIELD_NAME, Query.Direction.DESCENDING)

    private val queryBestRated = collection
        .orderBy(AVERAGE_GRADE_FIELD_NAME, Query.Direction.DESCENDING)

    /**
     * Load a given number of [Reviewable] by decreasing number of reviews.
     *
     * @param number: number of item to load
     *
     * @return a [QueryResult] containing the result as a list of reviewable
     */
    override fun loadMostRated(number: Long): QueryResult<List<T>> = load(queryMostRated, number)

    /**
     * Load a given number of [Reviewable] by decreasing average grade.
     *
     * @param number: number of item to load
     *
     * @return a [QueryResult] containing the result as a list of reviewable
     */
    override fun loadBestRated(number: Long): QueryResult<List<T>> = load(queryBestRated, number)

    /**
     * Defines the query associated to a search for a given pattern in the given field
     *
     * @param field: the field to match. If null, matches on the id
     * @param pattern: the pattern to match (matches on the beginning of the string)
     *
     * @return Query associated to the research
     */
    private fun querySearchFor(pattern: String, field: String? = null): Query = collection
        .let { ref ->
            if (field != null) {
                ref.orderBy(field)
            } else ref
        }
        .startAt(pattern)
        .endAt(pattern + SUFFIX_MATCHER)


    fun search(pattern: String, field: String? = null): QueryResult<List<T>> = load(
        querySearchFor(pattern, field),
        LIMIT_QUERY_SEARCH
    )

}