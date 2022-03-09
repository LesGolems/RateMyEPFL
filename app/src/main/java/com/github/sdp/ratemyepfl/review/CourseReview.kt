package com.github.sdp.ratemyepfl.review

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.time.LocalDate

@Serializable
class CourseReview private constructor(
    override val rating: ReviewRating,
    val title: String,
    override val comment: String,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate
) : Review() {

    companion object {
        fun serialize(review: CourseReview): String = Json.encodeToString(review)
        fun deserialize(review: String): CourseReview = Json.decodeFromString(review)
    }

    override fun toString(): String {
        return String.format(
            "Rating: %s \n Title: %s \n \t %s",
            rating.toString(),
            title,
            comment
        )
    }

    data class Builder(
        private var rate: ReviewRating? = null,
        private var title: String? = null,
        private var comment: String? = null,
        private var date: LocalDate? = null,
    ) {
        fun setRate(rate: ReviewRating) = apply {
            this.rate = rate
        }

        fun setTitle(title: String) = apply {
            this.title = title
        }

        fun setComment(comment: String) = apply {
            this.comment = comment
        }

        fun setDate(date: LocalDate) = apply {
            this.date = date
        }

        fun build(): CourseReview {
            val rate = this.rate
            val title = this.title
            val comment = this.comment
            val date = this.date

            if (rate != null && title != null && comment != null && date != null) {
                return CourseReview(
                    rate, title, comment, date
                )
            } else throw IllegalStateException("Cannot build a review made of null elements")
        }


    }

    @Serializer(forClass = LocalDate::class)
    object LocalDateSerializer : KSerializer<LocalDate> {
        override fun deserialize(decoder: Decoder): LocalDate {
            return LocalDate.parse(decoder.decodeString())
        }

        override fun serialize(encoder: Encoder, value: LocalDate) {
            encoder.encodeString(value.toString())
        }

        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)
    }

}