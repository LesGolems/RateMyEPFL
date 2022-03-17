package com.github.sdp.ratemyepfl.model.review

import android.util.Log
import com.github.sdp.ratemyepfl.Review
import com.google.firebase.firestore.DocumentSnapshot
import java.time.LocalDate

class ClassroomReview(rate: Int, comment: String, date : LocalDate) : Review(rate, comment, date) {
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