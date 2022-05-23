package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepositoryImpl
import com.google.firebase.firestore.PropertyName
import kotlinx.serialization.Serializable

@Serializable
data class Classroom constructor(
    val name: String = "",
    override val grade: Double = 0.0,
    override val numReviews: Int = 0,
    val roomKind: String? = null,
) : Reviewable() {

    override fun toString(): String = name

    override fun getId(): String = name

}
