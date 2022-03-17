package com.github.sdp.ratemyepfl.placeholder

import com.google.firebase.firestore.FirebaseFirestore

abstract class Repository<T> {
    protected val db = FirebaseFirestore.getInstance()

    abstract suspend fun add(value : T)

    abstract suspend fun remove(value : T)

    abstract suspend fun get(): Collection<T?>
}