package com.github.sdp.ratemyepfl.database

import com.google.firebase.firestore.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

interface Repository {
    companion object {
        const val DEFAULT_QUERY_LIMIT: Long = 10
        const val MAX_QUERY_LIMIT: Long = 50
    }
    /**
     * Retrieve a given number of items from the collection
     *
     * @param number: the number of element to retrieve
     *
     * @return a QuerySnapshot of the request
     */
    suspend fun take(number: Long): QuerySnapshot

    /**
     * Execute the given query and returns the result as a flow. This limit the number of item
     * retrieved from the database.
     *
     * @param query: the query to execute
     * @param limit: the maximal number of item to retrieved, [DEFAULT_QUERY_LIMIT] by default.
     * @param transformation: transformation (or mapping) to apply to the [QuerySnapshot]
     * Cannot exceed [MAX_QUERY_LIMIT]
     *
     * @return a flow containing the result wrapped inside a [QueryState]
     */
    fun <U> execute(
        query: Query,
        limit: Long = DEFAULT_QUERY_LIMIT,
        transformation: (QuerySnapshot) -> U,
    ): QueryResult<U>


    /**
     * Retrieve an element by id from the collection
     *
     * @param id: the unique identifier (or key) of the object to retrieve
     */
    suspend fun getById(id: String): DocumentSnapshot


    /**
     * @param id : the identifier of the item to delete
     */
    fun remove(id: String)

    /**
     * Add an item in the database
     *
     * @param item: object to add
     */
    fun add(item: FirestoreItem)

    /**
     * Returns the collection of the repository
     */
    fun collection(): CollectionReference

    /**
     * Returns the cloud database associated to the repository
     */
    fun database(): FirebaseFirestore
}