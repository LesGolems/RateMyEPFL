package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.Review
import java.time.LocalDate

class ClassroomReview(rate: Int, comment: String, date : LocalDate) : Review(rate, comment, date) {
}