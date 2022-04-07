package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.R
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RestaurantItemTest {
    private val LATLNG = LatLng(46.52, 6.569)
    private val NAME = "Arcadie"
    private val RESTAURANT_ITEM = RestaurantItem(LATLNG, NAME, R.raw.arcadie)

    @Test
    fun getPositionReturnsTheCorrectPosition() {
        assertEquals(LATLNG, RESTAURANT_ITEM.position)
    }

    @Test
    fun getTitleReturnsTheCorrectTitle() {
        assertEquals(NAME, RESTAURANT_ITEM.title)
    }

    @Test
    fun getSnippetReturnsNull() {
        assertNull(RESTAURANT_ITEM.snippet)
    }
}