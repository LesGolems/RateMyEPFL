package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.ReviewRepositoryInterface
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import java.time.LocalDate
import javax.inject.Inject

class FakeReviewsRepository @Inject constructor() : ReviewRepositoryInterface {

    companion object {
        val FAKE_UID_1 = "ID1"
        val FAKE_UID_2 = "ID2"
        val FAKE_UID_3 = "ID3"
        val FAKE_UID_4 = "ID4"

        val fakeList = listOf(
            Review.Builder().setTitle("Absolument dé-men-tiel")
                .setId("Fake")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setDate(LocalDate.now())
                .setLikers(listOf(FAKE_UID_1, FAKE_UID_2))
                .setDislikers(listOf(FAKE_UID_3, FAKE_UID_4))
                .build(),
            Review.Builder().setTitle("SA PARLE CASH")
                .setId("Fake")
                .setComment("Moi je cache rien, ça parle cash ici.")
                .setRating(ReviewRating.POOR)
                .setReviewableID("CS-453")
                .setDate(LocalDate.now())
                .setLikers(listOf(FAKE_UID_1, FAKE_UID_2, FAKE_UID_3))
                .build(),
            Review.Builder().setTitle("Allez-y, je pense à quel chiffre là ?")
                .setId("Fake")
                .setComment("Pfff n'importe quoi, je pensais à 1000.")
                .setRating(ReviewRating.TERRIBLE)
                .setReviewableID("CS-007")
                .setDate(LocalDate.now())
                .setDislikers(listOf(FAKE_UID_1, FAKE_UID_2, FAKE_UID_3))
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

    override fun add(value: HashMap<String, Any>) {}

    override suspend fun getReviews(): List<Review> {
        return reviewList
    }

    override suspend fun getReviewById(id: String): Review {
        return Review.Builder().setTitle("Absolument dé-men-tiel")
            .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
            .setRating(ReviewRating.EXCELLENT)
            .setReviewableID("CS-123")
            .setDate(LocalDate.now())
            .build()
    }

    override suspend fun getByReviewableId(id: String?): List<Review> {
        return reviewList
    }

    override fun addLiker(id: String, uid: String) {
        reviewList[0].likers = listOf(FAKE_UID_1, FAKE_UID_2, uid)
    }

    override fun removeLiker(id: String, uid: String) {
        reviewList[0].likers = listOf(FAKE_UID_1)
    }

    override fun addDisliker(id: String, uid: String) {
        reviewList[0].dislikers = listOf(FAKE_UID_2, FAKE_UID_3, FAKE_UID_4)
    }

    override fun removeDisliker(id: String, uid: String) {
        reviewList[0].dislikers = listOf(FAKE_UID_4)
    }
}