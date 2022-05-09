package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.R
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class EventItemTest {
    private val LATLNG = LatLng(46.52, 6.569)
    private val NAME = "Balélec"
    private val EVENT = Event(
        NAME, 0, 0, listOf(),
        LATLNG.latitude, LATLNG.longitude, LocalDateTime.now()
    )
    private val EVENT_ITEM = EventItem(EVENT, R.raw.arcadie, null)

    @Test
    fun getPositionReturnsTheCorrectPosition() {
        assertEquals(LATLNG, EVENT_ITEM.position)
    }

    @Test
    fun getTitleReturnsTheCorrectTitle() {
        assertEquals(NAME, EVENT_ITEM.title)
    }

    @Test
    fun getSnippetReturnsNull() {
        assertNull(EVENT_ITEM.snippet)
    }

    @Test
    fun onClickIntentReturnTheCorrectIntent() {
        assertNotNull(EVENT_ITEM.onClickIntent(null))
    }
}