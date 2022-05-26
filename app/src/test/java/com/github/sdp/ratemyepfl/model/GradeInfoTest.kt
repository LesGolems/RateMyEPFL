package com.github.sdp.ratemyepfl.model

import org.junit.Assert.assertEquals
import org.junit.Test

class GradeInfoTest {

    @Test
    fun constructorWithAllFieldsWorks() {
        val gradeInfo = GradeInfo("id", mapOf(Pair("rid", ReviewInfo(5, 5))))
        assertEquals("id", gradeInfo.itemId)
        assertEquals(5, gradeInfo.reviewsData["rid"]!!.reviewGrade)
        assertEquals(5, gradeInfo.reviewsData["rid"]!!.likeRatio)
    }
}