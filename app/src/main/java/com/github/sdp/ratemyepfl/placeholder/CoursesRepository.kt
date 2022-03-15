package com.github.sdp.ratemyepfl.placeholder

import com.github.sdp.ratemyepfl.model.items.Course
import com.google.firebase.firestore.FirebaseFirestore

class CoursesRepository (private val db : FirebaseFirestore) {

    suspend fun get() : Set<Course>{
        TODO()
    }



}