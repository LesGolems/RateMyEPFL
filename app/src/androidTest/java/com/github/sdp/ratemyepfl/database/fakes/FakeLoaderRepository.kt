package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.LoaderRepository
import com.github.sdp.ratemyepfl.database.RepositoryItem
import com.github.sdp.ratemyepfl.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.database.query.OrderedQuery
import com.github.sdp.ratemyepfl.database.query.Query
import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction
import org.mockito.Mockito
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class FakeLoaderRepository<T: RepositoryItem> @Inject constructor() : LoaderRepository<T>, FakeRepository<T>() {
    override fun load(query: OrderedQuery, number: UInt): QueryResult<List<T>> =
        QueryResult.success(listOf())


    override fun loaded(query: OrderedQuery): List<T>? = null

}