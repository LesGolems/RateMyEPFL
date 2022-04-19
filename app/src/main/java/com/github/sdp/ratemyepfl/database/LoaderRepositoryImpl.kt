package com.github.sdp.ratemyepfl.database

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

/**
 * Defines a repository that loads data on-the-fly. It abstracts the loading of the data in such a
 * way that the user is not aware of the limited number of requests. It manages a cache for each
 * query and ensure that the same data is not loaded twice.
 *
 * @param repository: the repository to decorate with [load]
 * @param transform: the transform to apply on a [DocumentSnapshot] to obtain a [T]
 */
class LoaderRepositoryImpl<T>(
    val repository: Repository,
    private val transform: (DocumentSnapshot) -> T?
) : Repository by repository, LoaderRepository<T> {

    private val loadedData: HashMap<Query, List<T>> = hashMapOf()

    override fun load(query: Query, number: Long): QueryResult<List<T>> {
        val updatedQuery = query.startAfter(loadedData[query])
        return execute(updatedQuery, number) { querySnapshot ->
            querySnapshot.mapNotNull { document ->
                transform(document)
            }
        }.mapResult { data ->
            updateData(query, data) }
    }

    /**
     * Add data to the loaded data
     *
     * @param data: data to append to the current loaded data
     *
     * @return a list containing the data loaded so far
     */
    private fun updateData(query: Query, data: List<T>): List<T> {
        val loaded = loadedData.getOrDefault(query, listOf())
        val updated = loaded + data
        loadedData[query] = updated
        return updated
    }
}