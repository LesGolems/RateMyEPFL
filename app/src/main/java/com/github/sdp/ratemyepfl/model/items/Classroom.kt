package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.model.review.ClassroomReview
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

}