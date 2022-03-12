package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.model.review.ClassroomReview

class Classroom(val id: String, val name: String, val reviews: List<ClassroomReview> = listOf())

    /*fun addReview(review: ClassroomReview) {
        reviews.add(review)
    }
}*/
