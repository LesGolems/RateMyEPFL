package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class ClassroomRepository @Inject constructor() :
    ClassroomRepositoryInterface, Repository(CLASSROOM_COLLECTION_PATH) {

    companion object {
        const val CLASSROOM_COLLECTION_PATH = "rooms"
        const val ROOM_KIND_FIELD = "roomKind"

        fun DocumentSnapshot.toClassroom(): Classroom? {
            val builder = Classroom.Builder()
                .setId(id)
                .setRoomKind(getString(ROOM_KIND_FIELD))
            return try {
                builder.build()
            } catch (e: IllegalStateException) {
                null
            }
        }
    }

    fun toItem(snapshot: DocumentSnapshot): Classroom? = snapshot.toClassroom()

    override suspend fun getClassrooms(): List<Classroom> {
        return take(DEFAULT_LIMIT).mapNotNull { obj -> toItem(obj) }
    }

    override suspend fun getRoomById(id: String): Classroom? = toItem(getById(id))
}