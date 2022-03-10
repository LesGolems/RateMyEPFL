package com.github.sdp.ratemyepfl

import com.github.sdp.ratemyepfl.review.ClassroomReview

class Classroom(val id: String, val name: String, val reviews: List<ClassroomReview> = listOf()) {

    /*fun addReview(review: ClassroomReview) {
        reviews.add(review)
    }*/

}