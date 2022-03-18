package com.github.sdp.ratemyepfl.model.items

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.Serializable

@Serializable
data class Classroom(
    override val id: String,
    override var numRatings: Int = 0,
    override var avgRating: Double = 0.0,
    val type: String? = null,
    val reviews: List<ClassroomReview>? = null
) : Reviewable()
{
    val name: String = type.orEmpty()

    constructor(id: String, name: String, reviews: List<ClassroomReview> = listOf())
        : this(id, 0, 0.0, name, reviews)

    // This converts a json object from firebase into a Classroom object
    companion object {
        fun DocumentSnapshot.toClassroom() : Classroom? {
            return try {
                //val id = getString("id")!!
                //val name = getString("name")!!
                Classroom(id, "",  listOf())
            } catch (e: Exception){
                null
            }
        }
        private const val TAG = "Classroom"
    }

}
