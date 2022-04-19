package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.QueryResult.Companion.asQueryResult
import com.github.sdp.ratemyepfl.database.Repository.Companion.DEFAULT_QUERY_LIMIT
import com.github.sdp.ratemyepfl.database.Repository.Companion.MAX_QUERY_LIMIT
import com.google.firebase.firestore.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Long.min

class RepositoryImpl(val database: FirebaseFirestore, collectionPath: String) : Repository {
    val collection = database.collection(collectionPath)

    override suspend fun take(number: Long): QuerySnapshot {
        return collection.limit(number).get().await()
    }

    override fun <U> execute(
        query: Query,
        limit: Long,
        transformation: (QuerySnapshot) -> U
    ): QueryResult<U> =
        flow {
            // Initiate the query by loading some data
            emit(QueryState.loading())

            // Execute the query
            val result: QuerySnapshot = query
                .limit(min(limit, MAX_QUERY_LIMIT))
                .get()
                .await()

            emit(QueryState.success(transformation(result)))
        }.catch {
            emit(QueryState.failure("Failed to execute the query $query"))
        }.asQueryResult()


    /**
     * Execute the given query and returns the result as a flow. This limit the number of item
     * retrieved from the database.
     *
     * @param query: the query to execute
     * @param limit: the maximal number of item to retrieved, [DEFAULT_QUERY_LIMIT] by default.
     * Cannot exceed [MAX_QUERY_LIMIT]
     *
     * @return a flow containing the result as a [QuerySnapshot] wrapped inside a [QueryState]
     */
    fun execute(
        query: Query,
        limit: Long = DEFAULT_QUERY_LIMIT
    ): QueryResult<QuerySnapshot> = execute(query, limit) { it }


    override suspend fun getById(id: String): DocumentSnapshot =
        collection.document(id).get().await()


    override fun remove(id: String) {
        collection.document(id).delete()
    }

    override fun add(item: FirestoreItem) {
        collection
            .document()
            .set(item.toHashMap())
    }

    override fun collection(): CollectionReference = collection

    override fun database(): FirebaseFirestore = database

}