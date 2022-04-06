package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import java.time.LocalDate
import javax.inject.Inject

class FakeReviewsRepository @Inject constructor() : ReviewsRepository {

    companion object {
        val fakeList = listOf(
            Review.Builder().setTitle("Absolument dé-men-tiel")
                .setId("Fake")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("SA PARLE CASH")
                .setId("Fake")
                .setComment("Moi je cache rien, ça parle cash ici.")
                .setRating(ReviewRating.POOR)
                .setReviewableID("CS-453")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("Allez-y, je pense à quel chiffre là ?")
                .setId("Fake")
                .setComment("Pfff n'importe quoi, je pensais à 1000.")
                .setRating(ReviewRating.TERRIBLE)
                .setReviewableID("CS-007")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setId("Fake")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setId("Fake")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setId("Fake")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setId("Fake")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setId("Fake")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setDate(LocalDate.now())
                .build(),
            Review.Builder().setTitle("The last review")
                .setId("Fake")
                .setComment("I am the last review")
                .setRating(ReviewRating.TERRIBLE)
                .setReviewableID("CS-303")
                .setDate(LocalDate.now())
                .build()
        )

        var reviewList = fakeList
    }

    override fun add(value: HashMap<String, String>) {}

    override suspend fun get(): List<Review> {
        return reviewList
    }

    override suspend fun getByReviewableId(id: String?): List<Review> {
        return reviewList
    }
}