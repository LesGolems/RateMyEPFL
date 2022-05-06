package com.github.sdp.ratemyepfl.utils

import android.content.pm.PackageManager
import org.junit.Assert.assertEquals
import org.junit.Test

class PermissionUtilsTest {

    @Test
    fun grantPermissionForPermissionInTheArray() {
        val grantPermissions: Array<String> = arrayOf("permission1", "permission2")
        val grantResults: IntArray =
            arrayOf(PackageManager.PERMISSION_DENIED, PackageManager.PERMISSION_GRANTED)
                .toIntArray()
        val permission = "permission2"

        assertEquals(
            true,
            PermissionUtils.isPermissionGranted(grantPermissions, grantResults, permission)
        )
    }

    @Test
    fun doNotGrantPermissionForPermissionInTheArray() {
        val grantPermissions: Array<String> = arrayOf("permission")
        val grantResults: IntArray = arrayOf(PackageManager.PERMISSION_GRANTED)
            .toIntArray()
        val permission = "unknown"

        assertEquals(
            false,
            PermissionUtils.isPermissionGranted(grantPermissions, grantResults, permission)
        )
    }

    @Test
    fun doNotGrantPermissionForPermissionDeniedInTheArray() {
        val grantPermissions: Array<String> = arrayOf("permission")
        val grantResults: IntArray = arrayOf(PackageManager.PERMISSION_DENIED)
            .toIntArray()
        val permission = "permission"

        assertEquals(
            false,
            PermissionUtils.isPermissionGranted(grantPermissions, grantResults, permission)
        )
    }
}