package com.github.sdp.ratemyepfl.backend.database.fakes

import com.github.sdp.ratemyepfl.backend.database.post.ReviewRepository
import com.github.sdp.ratemyepfl.backend.database.firebase.post.ReviewRepositoryImpl.Companion.toReview
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.model.time.DateTime
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import org.mockito.Mockito
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class FakeReviewRepository @Inject constructor() : ReviewRepository, FakeRepository<Review>() {

    companion object {
        const val FAKE_UID_1 = "ID1"
        const val FAKE_UID_2 = "ID2"
        const val FAKE_UID_3 = "ID3"
        const val FAKE_UID_4 = "ID4"
        val DATE = DateTime(2000, 1, 1, 0, 0)

        val fakeList = listOf(
            Review.Builder()
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setTitle("Absolument dé-men-tiel")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DATE)
                .setLikers(listOf(FAKE_UID_1, FAKE_UID_2))
                .setDislikers(listOf(FAKE_UID_3, FAKE_UID_4))
                .setUid(FakeUserRepository.UID1)
                .build(),
            Review.Builder()
                .setRating(ReviewRating.POOR)
                .setReviewableID("CS-453")
                .setTitle("SA PARLE CASH")
                .setComment("Moi je cache rien, ça parle cash ici.")
                .setDate(DATE)
                .setLikers(listOf(FAKE_UID_1, FAKE_UID_2, FAKE_UID_3))
                .setUid(FakeUserRepository.UID2)
                .build(),
            Review.Builder()
                .setRating(ReviewRating.TERRIBLE)
                .setReviewableID("CS-007")
                .setTitle("Allez-y, je pense à quel chiffre là ?")
                .setComment("Pfff n'importe quoi, je pensais à 1000.")
                .setDate(DATE)
                .setDislikers(listOf(FAKE_UID_1, FAKE_UID_2, FAKE_UID_3))
                .setUid(FakeUserRepository.UID3)
                .build(),
            Review.Builder()
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setDate(DATE)
                .build(),
            Review.Builder()
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setDate(DATE)
                .build(),
            Review.Builder()
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setDate(DATE)
                .build(),
            Review.Builder()
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setDate(DATE)
                .build(),
            Review.Builder()
                .setRating(ReviewRating.GOOD)
                .setReviewableID("CS-435")
                .setTitle("Ce mec ne fait qu'un avec le serpent")
                .setComment("Regardez comme il ondule. En forêt Amazonienne, je prendrais ce type pour un serpent... Il a tout du reptile !")
                .setDate(DATE)
                .build(),
            Review.Builder()
                .setRating(ReviewRating.TERRIBLE)
                .setReviewableID("CS-303")
                .setTitle("The last review")
                .setComment("I am the last review")
                .setDate(DATE)
                .build()
        )

        var reviewList = fakeList
    }

    override suspend fun getReviews(): List<Review> {
        return reviewList
    }

    override suspend fun getReviewById(id: String): Review {
        return Review.Builder()
            .setRating(ReviewRating.EXCELLENT)
            .setReviewableID("CS-123")
            .setTitle("Absolument dé-men-tiel")
            .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
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

    override suspend fun removeUpVote(postId: String, userId: String) {
        reviewList.map {
            if (it.getId() == postId) {
                it.copy(likers = it.likers.minus(userId))
            } else it
        }
    }

    override suspend fun addDownVote(postId: String, userId: String) {
        reviewList.map {
            if (it.getId() == postId) {
                it.copy(likers = it.dislikers.plus(userId))
            } else it
        }
    }

    override suspend fun removeDownVote(postId: String, userId: String) {
        reviewList.map {
            if (it.getId() == postId) {
                it.copy(likers = it.dislikers.minus(userId))
            } else it
        }
    }

    override fun addWithId(item: Review, withId: String): Task<String> {
        TODO("Not yet implemented")
    }

    override fun transform(document: DocumentSnapshot): Review? =
        document.toReview()

    override fun remove(id: String): Task<Void> {
        val newList = arrayListOf<Review>()

        for (r in reviewList) {
            if (r.getId() != id) {
                newList.add(r)
            }
        }

        reviewList = newList

        return Mockito.mock(Task::class.java) as Task<Void>
    }
}