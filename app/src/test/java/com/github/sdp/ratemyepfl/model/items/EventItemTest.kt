package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.R
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.*
import org.junit.Test

class EventItemTest {

    companion object {
        private val LAT_LNG = LatLng(46.52, 6.569)
        private const val NAME = "Balélec"
        private val EVENT = Event(
            NAME, NAME, 0, 0, listOf(), "", 0.0, 0,
            LAT_LNG.latitude, LAT_LNG.longitude
        )
        private val EVENT_ITEM = EventItem(EVENT, R.raw.arcadie, null)
    }


    @Test
    fun getPositionReturnsTheCorrectPosition() {
        assertEquals(LAT_LNG, EVENT_ITEM.position)
    }

    @Test
    fun getTitleReturnsTheCorrectTitle() {
        assertEquals(NAME, EVENT_ITEM.title)
    }

    @Test
    fun getSnippetReturnsNull() {
        assertNull(EVENT_ITEM.snippet)
    }
}