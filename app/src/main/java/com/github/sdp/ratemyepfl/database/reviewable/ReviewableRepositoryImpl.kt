package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.LoaderRepository
import com.github.sdp.ratemyepfl.database.LoaderRepositoryImpl
import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.RepositoryImpl
import com.github.sdp.ratemyepfl.database.SearchableRepository.Companion.LIMIT_QUERY_SEARCH
import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject


/**
 * Decorator for Repository that defines reviewable related operations
 *
 * @param repository: a [LoaderRepositoryImpl] to decorate
 * @param idFieldName: the name of the field that holds the id of the [Reviewable]
 */
class ReviewableRepositoryImpl<T : Reviewable> private constructor(
    private val repository: LoaderRepository<T>,
    private val idFieldName: String,
) : ReviewableRepository<T>, LoaderRepository<T> by repository {

    constructor(repository: Repository<T>, idFieldName: String) : this(
        LoaderRepositoryImpl(repository),
        idFieldName
    )

    constructor(
        database: FirebaseFirestore,
        collectionPath: String,
        idFieldName: String,
        transform: (DocumentSnapshot) -> T?
    ) : this(
        LoaderRepositoryImpl(RepositoryImpl(database, collectionPath, transform)),
        idFieldName
    )

    companion object {
        const val NUM_REVIEWS_FIELD_NAME = "numReviews"
        const val AVERAGE_GRADE_FIELD_NAME = "averageGrade"
    }


    private val queryMostRated = query()
        .orderBy(NUM_REVIEWS_FIELD_NAME, Query.Direction.DESCENDING)
        .orderBy(idFieldName)

    private val queryBestRated = repository
        .query()
        .orderBy(AVERAGE_GRADE_FIELD_NAME, Query.Direction.DESCENDING)
        .orderBy(idFieldName)

    /**
     * Load a given number of [Reviewable] by decreasing number of reviews.
     *
     * @param number: number of item to load
     *
     * @return a [QueryResult] containing the result as a list of reviewable
     */
    override fun loadMostRated(number: UInt): QueryResult<List<T>> = load(
        query()
            .orderBy(NUM_REVIEWS_FIELD_NAME, Query.Direction.DESCENDING)
            .orderBy(idFieldName), number
    )

    /**
     * Load a given number of [Reviewable] by decreasing average grade.
     *
     * @param number: number of item to load
     *
     * @return a [QueryResult] containing the result as a list of reviewable
     */
    override fun loadBestRated(number: UInt): QueryResult<List<T>> = load(queryBestRated, number)


    /**
     * Search for a matching prefix in a provided field.
     *
     * @param prefix: the prefix to match
     * @param field: the field where the match occurs
     *
     * @return a [QueryResult] containing a [List] of matched values. It matches at most
     * [LIMIT_QUERY_SEARCH] values.
     */
    fun search(field: String, prefix: String): QueryResult<List<T>> =
        repository
            .query()
            .match(field, prefix)
            .execute(LIMIT_QUERY_SEARCH)
            .mapResult { it.mapNotNull { document -> transform(document) } }


}