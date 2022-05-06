package com.github.sdp.ratemyepfl.utils

import android.os.Build
import android.view.KeyEvent
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

    fun grantPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            val allowPermission = uiDevice.findObject(
                UiSelector().text(
                    when {
                        Build.VERSION.SDK_INT == 23 -> "Allow"
                        Build.VERSION.SDK_INT <= 28 -> "ALLOW"
                        Build.VERSION.SDK_INT == 29 -> "Allow only while using the app"
                        else -> "While using the app"
                    }
                )
            )
            if (allowPermission.exists()) {
                allowPermission.click()
            }
        }
    }

    fun denyPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            val denyPermission = uiDevice.findObject(
                UiSelector().text(
                    when (Build.VERSION.SDK_INT) {
                        in 24..28 -> "DENY"
                        else -> "Deny"
                    }
                )
            )
            if (denyPermission.exists()) {
                denyPermission.click()
            }
        }
    }

    fun capturePhoto() {
        uiDevice.executeShellCommand("input keyevent ${KeyEvent.KEYCODE_CAMERA}")
    }

}