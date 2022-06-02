package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.model.review.Post.Companion.addIfAbsent
import org.junit.Assert.*
import org.junit.Test

class PostTest {
    @Test
    fun addIfAbsentAddANewElement() {
        val l = listOf(3, 4)
        assertEquals(l.plus(5), l.addIfAbsent(5))
    }

    @Test
    fun addIfAbsentDoesNotDuplicate() {
        val l = listOf(0)
        assertEquals(l, l.addIfAbsent(0))
    }
}