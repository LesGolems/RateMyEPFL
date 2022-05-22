package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.ReviewRepository
import com.github.sdp.ratemyepfl.database.ReviewRepositoryImpl.Companion.toReview
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.model.time.Date
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import org.mockito.Mockito
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class FakeReviewsRepository @Inject constructor() : ReviewRepository, FakeRepository<Review>(
    fakeList.first()
) {

    companion object {
        const val FAKE_UID_1 = "ID1"
        const val FAKE_UID_2 = "ID2"
        const val FAKE_UID_3 = "ID3"
        const val FAKE_UID_4 = "ID4"
        val DATE = Date(2000, 1, 1)

        val fakeList = listOf(
            Review.Builder().setTitle("Absolument dé-men-tiel")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setDate(DATE)
                .setLikers(listOf(FAKE_UID_1, FAKE_UID_2))
                .setDislikers(listOf(FAKE_UID_3, FAKE_UID_4))
                .setUid(FakeUserRepository.UID1)
                .build(),
            Review.Builder().setTitle("SA PARLE CASH")
                .setComment("Moi je cache rien, ça parle cash ici.")
                .setRating(ReviewRating.POOR)
                .setReviewableID("CS-453")
                .setDate(DATE)
                .setLikers(listOf(FAKE_UID_1, FAKE_UID_2, FAKE_UID_3))
                .setUid(FakeUserRepository.UID2)
                .build(),
            Review.Builder().setTitle("Allez-y, je pense à quel chiffre là ?")
                .setComment("Pfff n'importe quoi, je pensais à 1000.")
                .setRating(ReviewRating.TERRIBLE)
                .setReviewableID("CS-007")
                .setDate(DATE)
                .setDislikers(listOf(FAKE_UID_1, FAKE_UID_2, FAKE_UID_3))
                .setUid(FakeUserRepository.UID3)
                .build(),
            Review.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setDate(DATE)
                .build(),
            Review.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setDate(DATE)
                .build(),
            Review.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setDate(DATE)
                .build(),
            Review.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setDate(DATE)
                .build(),
            Review.Builder().setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setDate(DATE)
                .build(),
            Review.Builder().setTitle("The last review")
                .setComment("I am the last review")
                .setRating(ReviewRating.TERRIBLE)
                .setReviewableID("CS-303")
                .setDate(DATE)
                .build()
        )

        var reviewList = fakeList
    }

    override suspend fun getReviews(): List<Review> {
        return reviewList
    }

    override suspend fun getReviewById(id: String): Review {
        return Review.Builder().setTitle("Absolument dé-men-tiel")
            .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
            .setRating(ReviewRating.EXCELLENT)
            .setReviewableID("CS-123")
            .setDate(DATE)
            .build()
    }

    override suspend fun getByReviewableId(id: String?): List<Review> {
        return reviewList
    }

    override suspend fun addUpVote(reviewId: String, userId: String) {
        reviewList.map {
            if (it.getId() == reviewId) {
                it.copy(likers = it.likers.plus(userId))
            } else it
        }
    }

    override suspend fun removeUpVote(reviewId: String, userId: String) {
        reviewList.map {
            if (it.getId() == reviewId) {
                it.copy(likers = it.likers.minus(userId))
            } else it
        }
    }

    override suspend fun addDownVote(reviewId: String, userId: String) {
        reviewList.map {
            if (it.getId() == reviewId) {
                it.copy(likers = it.dislikers.plus(userId))
            } else it
        }
    }

    override suspend fun removeDownVote(reviewId: String, userId: String) {
        reviewList.map {
            if (it.getId() == reviewId) {
                it.copy(likers = it.dislikers.minus(userId))
            } else it
        }
    }

    override suspend fun addAndGetId(item: Review): String = "Nothing"

    override fun transform(document: DocumentSnapshot): Review? =
        document.toReview()

    override fun remove(id: String): Task<Void> {
        val newList = arrayListOf<Review>()

        for (r in reviewList){
            if(r.getId() != id){
                newList.add(r)
            }
        }

        reviewList = newList

        return Mockito.mock(Task::class.java) as Task<Void>
    }
}