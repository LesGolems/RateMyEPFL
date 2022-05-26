package com.github.sdp.ratemyepfl.backend.database.firebase

import com.github.sdp.ratemyepfl.backend.database.firebase.GradeInfoRepositoryImpl
import com.github.sdp.ratemyepfl.model.GradeInfo
import com.github.sdp.ratemyepfl.model.ReviewInfo
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class GradeInfoRepositoryTest {
    private val testGradeInfo = GradeInfo(
        "item id", mapOf(
            Pair("rid1", ReviewInfo(5, 5)),
            Pair("rid6", ReviewInfo(2, 0)),
            Pair("rid7", ReviewInfo(1, -4))
        )
    )

    private val testItem = Classroom("item id", 0.0, 0)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var gradeInfoRepo: GradeInfoRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
        runTest {
            gradeInfoRepo.add(testGradeInfo).await()
        }
    }

    @Test
    fun updateLikeRatioWorks() {
        runTest {
            gradeInfoRepo.updateLikeRatio(testItem, "rid1", -1).await()
            val gradeInfo = gradeInfoRepo.getGradeInfoById(testGradeInfo.itemId)
            assertNotNull(gradeInfo)
            assertNotNull(gradeInfo!!.reviewsData["rid1"])
            assertEquals(4, gradeInfo.reviewsData["rid1"]!!.likeRatio)
        }
    }

    @Test
    fun addReviewWorksWhenIdExists() {
        runTest {
            gradeInfoRepo.addReview(testItem, "rid3", ReviewRating.EXCELLENT).await()
            val gradeInfo = gradeInfoRepo.getGradeInfoById(testGradeInfo.itemId)
            assertNotNull(gradeInfo)
            assertNotNull(gradeInfo!!.reviewsData["rid3"])
            assertEquals(5, gradeInfo.reviewsData["rid3"]!!.reviewGrade)
        }
    }

    @Test
    fun removeReviewWorks() {
        runTest {
            gradeInfoRepo.addReview(testItem, "rid4", ReviewRating.EXCELLENT).await()
            gradeInfoRepo.removeReview(testItem, "rid4")
            val gradeInfo = gradeInfoRepo.getGradeInfoById(testGradeInfo.itemId)
            assertNotNull(gradeInfo)
            assertNull(gradeInfo!!.reviewsData["rid4"])
        }
    }

    @Test
    fun addReviewWorksWhenIdNotExists() {
        runTest {
            gradeInfoRepo.addReview(testItem.copy(name = "new id"), "rid3", ReviewRating.EXCELLENT)
                .await()
            val gradeInfo = gradeInfoRepo.getGradeInfoById("new id")
            assertNotNull(gradeInfo)
            assertNotNull(gradeInfo!!.reviewsData["rid3"])
            assertEquals(5, gradeInfo.reviewsData["rid3"]!!.reviewGrade)
        }
    }
}