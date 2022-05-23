package com.github.sdp.ratemyepfl.backend.database.fakes

import com.github.sdp.ratemyepfl.backend.database.Repository
import com.github.sdp.ratemyepfl.backend.database.RepositoryItem
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseQuery
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import org.mockito.Mockito

@Suppress("UNCHECKED_CAST")
open class FakeRepository<T : RepositoryItem> : Repository<T> {

    var elements: Set<T> = setOf()

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

    override fun query(): FirebaseQuery = FirebaseQuery(Mockito.mock(Query::class.java))
}