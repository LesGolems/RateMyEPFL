package com.github.sdp.ratemyepfl.utils

import com.github.sdp.ratemyepfl.R
import org.junit.Assert.assertEquals
import org.junit.Test

class MapActivityUtilsTest {
    @Test
    fun restaurantsMappingIsCorrect() {
        assertEquals(R.raw.arcadie, MapActivityUtils.PHOTO_MAPPING["Arcadie"])
    }
}