package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.ReviewRepository
import com.github.sdp.ratemyepfl.database.ReviewRepository.Companion.toReview
import com.github.sdp.ratemyepfl.database.ReviewRepositoryInterface
import com.github.sdp.ratemyepfl.database.query.Query
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction
import org.mockito.Mockito
import java.time.LocalDate
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class FakeReviewsRepository @Inject constructor() : ReviewRepositoryInterface {

    companion object {
        val FAKE_UID_1 = "ID1"
        val FAKE_UID_2 = "ID2"
        val FAKE_UID_3 = "ID3"
        val FAKE_UID_4 = "ID4"

        val fakeList = listOf(
            Review.Builder().setTitle("Absolument dé-men-tiel")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setDate(LocalDate.now())
                .setLikers(listOf(FAKE_UID_1, FAKE_UID_2))
                .setDislikers(listOf(FAKE_UID_3, FAKE_UID_4))
                .setUid(FakeUserRepository.UID1)
                .build(),
            Review.Builder().setTitle("SA PARLE CASH")
                .setComment("Moi je cache rien, ça parle cash ici.")
                .setRating(ReviewRating.POOR)
                .setReviewableID("CS-453")
                .setDate(LocalDate.now())
                .setLikers(listOf(FAKE_UID_1, FAKE_UID_2, FAKE_UID_3))
                .setUid(FakeUserRepository.UID2)
                .build(),
            Review.Builder().setTitle("Allez-y, je pense à quel chiffre là ?")
                .setComment("Pfff n'importe quoi, je pensais à 1000.")
                .setRating(ReviewRating.TERRIBLE)
                .setReviewableID("CS-007")
                .setDate(LocalDate.now())
                .setDislikers(listOf(FAKE_UID_1, FAKE_UID_2, FAKE_UID_3))
                .setUid(FakeUserRepository.UID3)
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
    }

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

    override suspend fun addUidInArray(fieldName: String, id: String, uid: String) {
        if (fieldName == ReviewRepository.LIKERS_FIELD_NAME) {
            reviewList[0].likers = listOf(FAKE_UID_1, FAKE_UID_2, uid)
        } else {
            reviewList[0].dislikers = listOf(FAKE_UID_2, FAKE_UID_3, FAKE_UID_4)
        }
    }

    override suspend fun removeUidInArray(fieldName: String, id: String, uid: String) {
        if (fieldName == ReviewRepository.LIKERS_FIELD_NAME) {
            reviewList[0].likers = listOf(FAKE_UID_1)
        } else {
            reviewList[0].dislikers = listOf(FAKE_UID_4)
        }
    }

    override suspend fun take(number: Long): QuerySnapshot {
        return Mockito.mock(QuerySnapshot::class.java)
    }

    override suspend fun getById(id: String): DocumentSnapshot =
        Mockito.mock(DocumentSnapshot::class.java)

    override fun remove(id: String): Task<Void> = Mockito.mock(Task::class.java) as Task<Void>

    override fun add(item: Review) = Mockito.mock(Task::class.java) as Task<Void>
    override fun update(id: String, transform: (Review) -> Review): Task<Transaction> {
        return Mockito.mock(Task::class.java) as Task<Transaction>
    }

    override fun transform(document: DocumentSnapshot): Review? =
        document.toReview()


    override fun query(): Query = Mockito.mock(Query::class.java)
}