package com.github.sdp.ratemyepfl.adapter.util

import org.junit.Assert.assertEquals
import org.junit.Test

class AdapterUtilTest {

    @Test
    fun sameItemTest() {
        val diff = AdapterUtil.diffCallback<Int>()
            .areItemsTheSame(5, 5)
        assertEquals(true, diff)
    }

    @Test
    fun differentItemTest() {
        val diff = AdapterUtil.diffCallback<Int>()
            .areItemsTheSame(5, 6)
        assertEquals(false, diff)
    }

    @Test
    fun sameContentItemTest() {
        val diff = AdapterUtil.diffCallback<Item>()
            .areContentsTheSame(
                Item("id1", "content", "some garbade"),
                Item("id2", "content", "a lot of garbage")
            )
        assertEquals(true, diff)
    }

    @Test
    fun differentContentItemTest() {
        val diff = AdapterUtil.diffCallback<Item>()
            .areContentsTheSame(
                Item("id1", "content1", "some garbade"),
                Item("id2", "content2", "a lot of garbage")
            )
        assertEquals(false, diff)
    }

    class Item(val id: String, val content: String, val garbage: String) {
        override fun toString(): String {
            return content
        }
    }
}