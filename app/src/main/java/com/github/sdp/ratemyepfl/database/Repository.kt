package com.github.sdp.ratemyepfl.database

import com.google.firebase.firestore.FirebaseFirestore

open class Repository {
    protected val db = FirebaseFirestore.getInstance()
}