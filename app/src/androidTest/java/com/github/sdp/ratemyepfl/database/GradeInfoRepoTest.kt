package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.GradeInfo
import com.github.sdp.ratemyepfl.model.ReviewInfo
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class GradeInfoRepoTest {
    private val testGradeInfo = GradeInfo("item id", mapOf(Pair("rid1", ReviewInfo(5, 5))))

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var gradeInfoRepo: GradeInfoRepositoryImpl

    @Before
    fun setup(){
        hiltRule.inject()
        runTest {
            gradeInfoRepo.add(testGradeInfo).await()
        }
    }

    @Test
    fun updateLikeRatioWorks(){
        runTest {
            gradeInfoRepo.updateLikeRatio(testGradeInfo.itemId, "rid1", -1).await()
            val gradeInfo = gradeInfoRepo.getGradeInfoById(testGradeInfo.itemId)
            assertNotNull(gradeInfo)
            assertNotNull(gradeInfo!!.reviewsData["rid1"])
            assertEquals(4, gradeInfo.reviewsData["rid1"]!!.likeRatio)
        }
    }

    @Test
    fun addReviewWorksWhenIdExists(){
        runTest {
            gradeInfoRepo.addReview(testGradeInfo.itemId, "rid3", ReviewRating.EXCELLENT).await()
            val gradeInfo = gradeInfoRepo.getGradeInfoById(testGradeInfo.itemId)
            assertNotNull(gradeInfo)
            assertNotNull(gradeInfo!!.reviewsData["rid3"])
            assertEquals(5, gradeInfo.reviewsData["rid3"]!!.reviewGrade)
        }
    }

    @Test
    fun addReviewWorksWhenIdNotExists(){
        runTest {
            gradeInfoRepo.addReview("new id", "rid3", ReviewRating.EXCELLENT).await()
            val gradeInfo = gradeInfoRepo.getGradeInfoById("new id")
            assertNotNull(gradeInfo)
            assertNotNull(gradeInfo!!.reviewsData["rid3"])
            assertEquals(5, gradeInfo.reviewsData["rid3"]!!.reviewGrade)
        }
    }
}