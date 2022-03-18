package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.serializer.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
abstract class Review() {
    abstract val rating: ReviewRating
    abstract val title: String
    abstract val comment: String
    @Serializable(with = LocalDateSerializer::class)
    abstract val date: LocalDate
}