package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import java.time.LocalDate
import javax.inject.Inject

class FakeReviewsRepository @Inject constructor() : ReviewsRepository {

    companion object {
        val fakeList = listOf(
            Review.Builder().setTitle("Absolument dé-men-tiel")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("SA PARLE CASH")
                .setComment("Moi je cache rien, ça parle cash ici.")
                .setRating(ReviewRating.POOR)
                .setReviewableID("CS-453")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("Allez-y, je pense à quel chiffre là ?")
                .setComment("Pfff n'importe quoi, je pensais à 1000.")
                .setRating(ReviewRating.TERRIBLE)
                .setReviewableID("CS-007")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("The last review")
                .setComment("I am the last review")
                .setRating(ReviewRating.TERRIBLE)
                .setReviewableID("CS-303")
                .setDate(LocalDate.now())
                .build()
        )

        var reviewList = fakeList

        val fakePhotoList = listOf(
            R.drawable.room3,
            R.drawable.room1,
            R.drawable.room4,
            R.drawable.room2,
            R.drawable.room5,
            R.drawable.room6
        )

        var photoList = fakePhotoList
    }

    override fun add(value: Review) {}

    override suspend fun get(): List<Review> {
        return reviewList
    }

    override suspend fun getByReviewableId(id: String?): List<Review> {
        return reviewList
    }

    override suspend fun getPhotosByReviewableId(id: String?): List<Int> {
        return photoList
    }
}