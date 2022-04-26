package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.query.OrderedQuery
import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.database.query.Queryable
import com.google.firebase.firestore.DocumentSnapshot

/**
 * Defines a repository that loads data on-the-fly. It abstracts the loading of the data in such a
 * way that the user is not aware of the limited number of requests. It manages a cache for each
 * query and ensure that the same data is not loaded twice.
 *
 * @param repository: the repository to decorate with [load]
 * @param transform: the transform to apply on a [DocumentSnapshot] to obtain a [T]
 */
class LoaderRepositoryImpl<T : FirestoreItem>(
    val repository: RepositoryImpl<T>,
    val transform: (DocumentSnapshot) -> T?
) : Repository<T> by repository, LoaderRepository<T>, Queryable by repository {

    internal val collection = repository.collection
    internal val database = repository.database

    private val loadedData: HashMap<OrderedQuery, List<T>> = hashMapOf()
    private val lastLoaded: HashMap<OrderedQuery, DocumentSnapshot> = hashMapOf()

    override fun load(query: OrderedQuery, number: UInt): QueryResult<List<T>> {
            val lastLoaded = query.fields.map { lastLoaded[query]?.get(it) }
            val updatedQuery = query.startAfter(lastLoaded)
            return updatedQuery.execute(number)
                .mapResult { querySnapshot ->
                    querySnapshot.documents.filterNotNull()
                }.mapResult { data ->
                    updateData(query, data)
                }
        }


    /**
     * Add data to the loaded data
     *
     * @param data: data to append to the current loaded data
     *
     * @return a list containing the data loaded so far
     */
    private fun updateData(query: OrderedQuery, data: List<DocumentSnapshot>): List<T> {
        val loaded = loadedData.getOrDefault(query, listOf())
        return if (data.isNotEmpty()) {
            val last = data.last()
            val updated = loaded + data.mapNotNull(transform)
            loadedData[query] = updated
            lastLoaded[query] = last
            updated
        } else loaded
    }
}