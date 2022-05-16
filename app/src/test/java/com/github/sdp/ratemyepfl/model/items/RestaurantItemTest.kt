package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.R
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.*
import org.junit.Test

class RestaurantItemTest {

    companion object {
        private val LAT_LNG = LatLng(46.52, 6.569)
        private const val NAME = "Arcadie"
        private val RESTAURANT = Restaurant(NAME, 0, 0.0, 0, LAT_LNG.latitude, LAT_LNG.longitude)
        private val RESTAURANT_ITEM = RestaurantItem(RESTAURANT, R.raw.arcadie, null)
    }

    @Test
    fun getPositionReturnsTheCorrectPosition() {
        assertEquals(LAT_LNG, RESTAURANT_ITEM.position)
    }

    @Test
    fun getTitleReturnsTheCorrectTitle() {
        assertEquals(NAME, RESTAURANT_ITEM.title)
    }

    @Test
    fun getSnippetReturnsNull() {
        assertNull(RESTAURANT_ITEM.snippet)
    }

    @Test
    fun onClickIntentReturnTheCorrectIntent() {
        assertNotNull(RESTAURANT_ITEM.onClickIntent(null))
    }
}