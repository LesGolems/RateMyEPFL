package com.github.sdp.ratemyepfl.model.review
import android.util.Log
import com.github.sdp.ratemyepfl.serializer.LocalDateSerializer
import com.google.firebase.firestore.DocumentSnapshot
import java.time.LocalDate
import kotlinx.serialization.Serializable

@Serializable
class ClassroomReview(override val rating: ReviewRating,
                      override val title: String,
                      override val comment: String,
                      @Serializable(with = LocalDateSerializer::class)
                      override val date: LocalDate) : Review() {
    val rate: Int = rating.rating
        get() {
            return field
        }

    constructor(rate: Int, comment: String, date : LocalDate)
            : this(ReviewRating.values()[rate * 5 / 100], "", comment, date)

    companion object {
        fun DocumentSnapshot.toClassroomReview() : ClassroomReview? {
            return try {
                val rate = getString("rate")!!.toInt()
                val comment = getString("comment")!!
                val date = LocalDate.parse(getString("date")!!)
                ClassroomReview(rate , comment, date)
            } catch (e: Exception){
                Log.e(TAG, "Error converting course review", e)
                null
            }
        }
        private const val TAG = "Classroom review"
    }

    fun toHashMap(): HashMap<String, String> {
        return hashMapOf("rate" to rate.toString(), "comment" to comment,
                         "date" to date.toString())
    }
}