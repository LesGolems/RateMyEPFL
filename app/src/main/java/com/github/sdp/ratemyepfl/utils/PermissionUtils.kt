package com.github.sdp.ratemyepfl.utils

import android.content.pm.PackageManager

object PermissionUtils {
    fun isPermissionGranted(
        grantPermissions: Array<String>, grantResults: IntArray,
        permission: String
    ): Boolean {
        // Arrays are supposed to be of the same length
        assert(grantPermissions.size <= grantResults.size)
        for (i in grantPermissions.indices) {
            if (permission == grantPermissions[i]) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED
            }
        }
        return false
    }
}