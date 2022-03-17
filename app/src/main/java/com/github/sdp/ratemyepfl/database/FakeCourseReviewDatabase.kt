package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.review.CourseReview
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import java.time.LocalDate
import javax.inject.Inject

class FakeCourseReviewDatabase @Inject constructor() : CourseReviewDatabase {
    private val reviewList: MutableList<CourseReview> = mutableListOf()

    init {
        reviewList.addAll(listOf(
            CourseReview.Builder().setTitle("Absolument dé-men-tiel")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setRating(ReviewRating.EXCELLENT)
                .setDate(LocalDate.now())
                .build(),
            CourseReview.Builder().setTitle("SA PARLE CASH")
                .setComment("Moi je cache rien, ça parle cash ici.")
                .setRating(ReviewRating.POOR)
                .setDate(LocalDate.now())
                .build(),
            CourseReview.Builder().setTitle("Allez-y, je pense à quel chiffre là ?")
                .setComment("Pfff n'importe quoi, je pensais à 1000.")
                .setRating(ReviewRating.TERRIBLE)
                .setDate(LocalDate.now())
                .build(),
            CourseReview.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setDate(LocalDate.now())
                .build()
        ))
    }

    override fun addReview(review: CourseReview){
        reviewList.add(review)
    }

    override fun getReviews(): List<CourseReview> = reviewList.toList()
}