package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.LoaderRepository
import com.github.sdp.ratemyepfl.database.RepositoryItem
import com.github.sdp.ratemyepfl.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.database.query.OrderedQuery
import com.github.sdp.ratemyepfl.database.query.Query
import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction
import org.mockito.Mockito
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class FakeLoaderRepository<T : RepositoryItem> @Inject constructor() : LoaderRepository<T> {
    override fun load(query: OrderedQuery, number: UInt): QueryResult<List<T>> =
        QueryResult.success(listOf())


    override fun loaded(query: OrderedQuery): List<T>? = null

    override suspend fun take(number: Long): QuerySnapshot = Mockito.mock(QuerySnapshot::class.java)

    override suspend fun getById(id: String): DocumentSnapshot =
        Mockito.mock(DocumentSnapshot::class.java)

    override fun remove(id: String): Task<Void> = Mockito.mock(Task::class.java) as Task<Void>

    override fun add(item: T): Task<Void> = Mockito.mock(Task::class.java) as Task<Void>

    override fun update(id: String, transform: (T) -> T): Task<Transaction> =
        Mockito.mock(Task::class.java) as Task<Transaction>

    override fun transform(document: DocumentSnapshot): T? = null

    override fun query(): Query = Query(Mockito.mock(FirebaseQuery::class.java))

}