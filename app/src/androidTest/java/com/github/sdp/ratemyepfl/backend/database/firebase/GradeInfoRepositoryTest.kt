package com.github.sdp.ratemyepfl.backend.database.firebase

import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.ClassroomRepositoryImpl
import com.github.sdp.ratemyepfl.model.GradeInfo
import com.github.sdp.ratemyepfl.model.ReviewInfo
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.After
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

    private val testItem = Classroom(testGradeInfo.itemId, 0.0, 0)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var gradeInfoRepo: GradeInfoRepositoryImpl

    @Inject
    lateinit var classroomRepo: ClassroomRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
        assertEquals(testGradeInfo.getId(), testItem.getId())
        runTest {
            gradeInfoRepo.add(testGradeInfo).collect()
            classroomRepo.add(testItem).collect()
        }
    }

    @After
    fun tearDown() = runTest {
        gradeInfoRepo.remove(testGradeInfo.getId()).collect()
        classroomRepo.remove(testItem.getId()).collect()
    }

    @Test
    fun updateLikeRatioWorks() {
        runTest {
            gradeInfoRepo.updateLikeRatio(testItem, "rid1", -1)
            val gradeInfo = gradeInfoRepo.getGradeInfoById(testGradeInfo.itemId)
            assertNotNull(gradeInfo)
            assertNotNull(gradeInfo!!.reviewsData["rid1"])
            assertEquals(4, gradeInfo.reviewsData["rid1"]!!.likeRatio)
        }
    }

    @Test
    fun addReviewWorksWhenIdExists() {
        runTest {
            gradeInfoRepo.addReview(testItem, "rid3", ReviewRating.EXCELLENT)
            val gradeInfo = gradeInfoRepo.getGradeInfoById(testGradeInfo.itemId)
            assertNotNull(gradeInfo)
            assertNotNull(gradeInfo!!.reviewsData["rid3"])
            assertEquals(5, gradeInfo.reviewsData["rid3"]!!.reviewGrade)
        }
    }

    @Test
    fun removeReviewWorks() {
        runTest {
            gradeInfoRepo.addReview(testItem, "rid4", ReviewRating.EXCELLENT)
            gradeInfoRepo.removeReview(testItem, "rid4")
            val gradeInfo = gradeInfoRepo.getGradeInfoById(testGradeInfo.itemId)
            assertNotNull(gradeInfo)
            assertNull(gradeInfo!!.reviewsData["rid4"])
        }
    }

    @Test
    fun addReviewWorksWhenIdNotExists() {
        runTest {
            val item = testItem.copy(name = "new id")
            classroomRepo.add(item).collect()
            gradeInfoRepo.addReview(item, "rid3", ReviewRating.EXCELLENT)
            val gradeInfo = gradeInfoRepo.getGradeInfoById("new id")
            assertNotNull(gradeInfo)
            assertNotNull(gradeInfo!!.reviewsData["rid3"])
            assertEquals(5, gradeInfo.reviewsData["rid3"]!!.reviewGrade)
            classroomRepo.remove(item.getId()).collect()
        }
    }
}