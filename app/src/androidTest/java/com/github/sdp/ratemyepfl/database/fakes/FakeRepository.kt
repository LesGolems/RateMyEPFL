package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.RepositoryImpl.Companion.toItem
import com.github.sdp.ratemyepfl.database.RepositoryItem
import com.github.sdp.ratemyepfl.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.database.query.Query
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import org.mockito.Mockito
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
open class FakeRepository<T : RepositoryItem> constructor(val defaultValue: T) : Repository<T> {

    var elements: Set<T> = setOf(defaultValue)

    override suspend fun take(number: Long): List<T> = elements.toList()

    override suspend fun getById(id: String): T? =
        elements.firstOrNull { it.getId() == id }

    override fun remove(id: String): Task<Void> {
        elements = elements.filter { it.getId() != id }.toSet()
        return Mockito.mock(Task::class.java) as Task<Void>
    }

    override fun add(item: T): Task<String> {
        elements = elements.plus(item)
        return Mockito.mock(Task::class.java) as Task<String>
    }

    override fun update(id: String, transform: (T) -> T): Task<T> {
        elements.map {
                if (it.getId() == id) {
                    transform(it)
                } else it
        }
        return Mockito.mock(Task::class.java) as Task<T>
    }


    override fun transform(document: DocumentSnapshot): T? = null

    override fun query(): Query = Query(Mockito.mock(FirebaseQuery::class.java))
}