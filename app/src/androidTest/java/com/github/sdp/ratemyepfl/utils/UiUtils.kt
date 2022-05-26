package com.github.sdp.ratemyepfl.utils

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector

object UiUtils {

    private val uiDevice: UiDevice =
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    fun objectWithDescription(desc: String): UiObject {
        return uiDevice.findObject(
            UiSelector().descriptionContains(desc)
        )
    }

}