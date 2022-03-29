package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Classroom.Companion.toClassroom
import com.github.sdp.ratemyepfl.model.items.Course.Companion.toCourse
import com.google.firebase.firestore.DocumentSnapshot

class ClassroomRepository: ItemsRepository<Classroom>(CLASSROOM_COLLECTION_PATH) {

    companion object {
        const val CLASSROOM_COLLECTION_PATH = "classrooms"
    }

    override fun toItem(snapshot: DocumentSnapshot): Classroom = snapshot.toClassroom()

}