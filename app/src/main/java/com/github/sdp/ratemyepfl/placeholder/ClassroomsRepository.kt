package com.github.sdp.ratemyepfl.placeholder

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.google.firebase.firestore.FirebaseFirestore

class ClassroomsRepository (db : FirebaseFirestore): Repository<Classroom>(db){

    override suspend fun add(value: Classroom) {
    }

    override suspend fun remove(value: Classroom) {
    }

    override suspend fun get() : Set<Classroom>{
    }


}