package com.github.sdp.ratemyepfl.placeholder

import com.github.sdp.ratemyepfl.model.items.Course
import com.google.firebase.firestore.FirebaseFirestore

class CoursesRepository (db : FirebaseFirestore) : Repository<Course>(db) {


    override suspend fun add(value: Course) {
    }

    override suspend fun remove(value: Course) {
    }

    override suspend fun get() : Set<Course>{
    }



}