package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.Serializable

@Serializable
abstract class Reviewable
{
    abstract val id: String
    abstract var numRatings: Int
    abstract var avgRating: Double
}