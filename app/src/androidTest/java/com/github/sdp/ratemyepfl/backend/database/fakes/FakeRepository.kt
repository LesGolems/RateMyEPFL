package com.github.sdp.ratemyepfl.backend.database.fakes

import com.github.sdp.ratemyepfl.backend.database.Repository
import com.github.sdp.ratemyepfl.backend.database.RepositoryItem
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseQuery
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mockito.Mockito

@Suppress("UNCHECKED_CAST")
open class FakeRepository<T : RepositoryItem> : Repository<T> {

    var elements: Set<T> = setOf()

    override fun get(number: Long): Flow<List<T>> = flow {
        emit(elements.toList())
    }

    override fun getById(id: String): Flow<T> = flow {
        elements.firstOrNull { it.getId() == id }
            ?.run { emit(this) }
    }

    override fun remove(id: String): Flow<Boolean> {
        elements = elements.filter { it.getId() != id }.toSet()
        return flow { emit(true) }
    }

    override fun add(item: T): Flow<String> {
        elements = elements.plus(item)
        return flow { emit(item.getId()) }
    }

    override fun update(id: String, transform: (T) -> T): Flow<T> = flow {
        elements.map {
            if (it.getId() == id) {
                val res = transform(it)
                emit(res)
                res
            } else it
        }
    }


    override fun transform(document: DocumentSnapshot): T? = null

    override fun query(): FirebaseQuery = FirebaseQuery(Mockito.mock(Query::class.java))
}