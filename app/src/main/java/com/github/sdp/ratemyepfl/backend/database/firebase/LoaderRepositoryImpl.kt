package com.github.sdp.ratemyepfl.backend.database.firebase

import com.github.sdp.ratemyepfl.backend.database.LoaderRepository
import com.github.sdp.ratemyepfl.backend.database.Repository
import com.github.sdp.ratemyepfl.backend.database.RepositoryItem
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseOrderedQuery
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseOrderedQuery.OrderedField.Companion.names
import com.github.sdp.ratemyepfl.backend.database.query.QueryResult
import com.google.firebase.firestore.DocumentSnapshot

/**
 * Defines a repository that loads data on-the-fly. It abstracts the loading of the data in such a
 * way that the user is not aware of the limited number of requests. It manages a cache for each
 * query and ensure that the same data is not loaded twice.
 *
 * @param repository: the repository to decorate with [load]
 */
class LoaderRepositoryImpl<T : RepositoryItem>(
    val repository: RepositoryImpl<T>,
) : Repository<T> by repository, LoaderRepository<T> {

    private var loadedData: HashMap<FirebaseOrderedQuery, List<T>> = hashMapOf()
    private var lastLoaded: HashMap<FirebaseOrderedQuery, DocumentSnapshot> = hashMapOf()

    override fun load(query: FirebaseOrderedQuery, number: UInt): QueryResult<List<T>> {
        val lastLoaded = query.fields
            .names()
            .map { lastLoaded[query]?.get(it) }
        val updatedQuery =
            if (lastLoaded.all { it != null }) query.startAfter(lastLoaded) else query
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
    private fun updateData(query: FirebaseOrderedQuery, data: List<DocumentSnapshot>): List<T> {
        val loaded = loadedData.getOrDefault(query, listOf())
        return if (data.isNotEmpty()) {
            val last = data.last()
            val updated = loaded + data.mapNotNull { transform(it) }
            loadedData[query] = updated
            lastLoaded[query] = last
            updated
        } else loaded
    }

    override fun resetLoaded() {
        loadedData = hashMapOf()
        lastLoaded = hashMapOf()
    }

    override fun loaded(query: FirebaseOrderedQuery): List<T>? =
        loadedData[query]

}