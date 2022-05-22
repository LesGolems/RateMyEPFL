package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.RepositoryItem
import com.github.sdp.ratemyepfl.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.database.query.Query
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import org.mockito.Mockito
import javax.inject.Inject

open class FakeRepository<T : RepositoryItem> constructor(val defaultValue: T) : Repository<T> {

    override suspend fun take(number: Long): List<T> = listOf(defaultValue)

    override suspend fun getById(id: String): T? =
        defaultValue

    override fun remove(id: String): Task<Void> = Mockito.mock(Task::class.java) as Task<Void>

    override fun add(item: T): Task<String> = Mockito.mock(Task::class.java) as Task<String>

    override fun update(id: String, transform: (T) -> T): Task<T> =
        Mockito.mock(Task::class.java) as Task<T>

    override fun transform(document: DocumentSnapshot): T? = null

    override fun query(): Query = Query(Mockito.mock(FirebaseQuery::class.java))
}