package com.github.sdp.ratemyepfl.backend.database.fakes

import com.github.sdp.ratemyepfl.backend.database.LoaderRepository
import com.github.sdp.ratemyepfl.backend.database.RepositoryItem
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseOrderedQuery
import com.github.sdp.ratemyepfl.backend.database.query.QueryResult
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class FakeLoaderRepository<T : RepositoryItem> @Inject constructor() : LoaderRepository<T>,
    FakeRepository<T>() {
    override fun load(query: FirebaseOrderedQuery, number: UInt): QueryResult<List<T>> =
        QueryResult.success(elements.toList())


    override fun loaded(query: FirebaseOrderedQuery): List<T>? = null
    override fun resetLoaded() {

    }
}