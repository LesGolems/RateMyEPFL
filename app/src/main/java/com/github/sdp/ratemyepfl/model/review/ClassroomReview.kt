package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.serializer.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
class ClassroomReview(    override val rating: ReviewRating,
                          override val title: String,
                          override val comment: String,
                          @Serializable(with = LocalDateSerializer::class)
                          override val date: LocalDate) : Review() {

    /**
     * For compatibility issues with current code and type that does not match database
     * as well as unbounded Int value used that does not match ReviewRating range of values.
     * e.g. some part of the code uses rating values like 15, here I assume it goes up to 100.
     */

    val rate: Int = rating.rating
        get() {
            return field
        }

    constructor(rate: Int, comment: String, date : LocalDate)
            : this(ReviewRating.values()[rate * 5 / 100], "", comment, date)
}