package com.github.sdp.ratemyepfl.model

import org.junit.Assert
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

    @Test
    fun builderWorks() {
        val builder = GradeInfo.Builder("id")
        val g = builder.build()
        assertEquals("id", g.itemId)
    }

    @Test
    fun builderThrowsForMissingId() {
        val builder = GradeInfo.Builder(null)

        Assert.assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }
}