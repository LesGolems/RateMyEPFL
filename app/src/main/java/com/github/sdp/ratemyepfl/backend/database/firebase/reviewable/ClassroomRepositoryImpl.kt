package com.github.sdp.ratemyepfl.backend.database.firebase.reviewable

import com.github.sdp.ratemyepfl.backend.database.LoaderRepository
import com.github.sdp.ratemyepfl.backend.database.firebase.LoaderRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.RepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.RepositoryImpl.Companion.toItem
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.backend.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.backend.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class ClassroomRepositoryImpl private constructor(private val repository: LoaderRepository<Classroom>) :
    ClassroomRepository,
    ReviewableRepository<Classroom>,
    LoaderRepository<Classroom> by repository {

    override val offlineData = OFFLINE_CLASSROOMS

    @Inject
    constructor(db: FirebaseFirestore) : this(
        LoaderRepositoryImpl<Classroom>(
            RepositoryImpl(db, CLASSROOM_COLLECTION_PATH)
            { documentSnapshot ->
                documentSnapshot.toClassroom()
            })
    )

    companion object {
        const val CLASSROOM_COLLECTION_PATH = "rooms"
        const val ROOM_NAME_FIELD_NAME = "name"

        val OFFLINE_CLASSROOMS: List<Classroom> = listOf(
            Classroom("BC233", 0.0, 0),
            Classroom("CE 1 1", 0.0, 0),
            Classroom("CE 1 4", 0.0, 0),
            Classroom("CM 1 1", 0.0, 0),
            Classroom("CM 1 3", 0.0, 0),
            Classroom("CM 1 4", 0.0, 0),
            Classroom("CM 1 5", 0.0, 0),
            Classroom("ELA 1", 0.0, 0),
        )

        fun DocumentSnapshot.toClassroom(): Classroom? = toItem()

    }

    override suspend fun getClassrooms() = repository.get(FirebaseQuery.MAX_QUERY_LIMIT.toLong())
        .last()


    override suspend fun getRoomByName(name: String) = repository.getById(name)
        .last()


}