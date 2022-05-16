package com.github.sdp.ratemyepfl.utils

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

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

    /**
     * Checks whether [permission] is granted in the current [Context].
     */
    private fun hasPermission(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    /**
     * Checks whether all [permissions] are granted in the current [Context].
     */
    private fun hasAllPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all { hasPermission(context, it) }

    /**
     * Starts a feature of the phone that requires one or more permission(s) to be used
     * (e.g., location, camera, storage).
     *
     * @param useFeature The function that uses the feature
     * @param resultLauncher checks whether the user chose to grant or deny runtime permissions
     * @param context The current context
     * @param permissions The permissions that need be to granted by the user
     */
    fun startPhoneFeature(
        useFeature: () -> Unit,
        resultLauncher: ActivityResultLauncher<Array<String>>,
        context: Context,
        vararg permissions: String
    ) {
        val perms: Array<String> = permissions.map { it }.toTypedArray()
        if (hasAllPermissions(context, perms)) {
            // The permission was already granted
            useFeature()
        } else {
            // Request the user to grant the permission at runtime
            resultLauncher.launch(perms)
        }
    }

    /**
     * Checks the user's response, whether they chose to grant or deny runtime permissions.
     *
     * @param useFeature The function that uses the feature
     * @param caller The Fragment/Activity from which the feature is started
     * @param context The current context
     */
    fun requestPermissionLauncher(
        useFeature: () -> Unit,
        caller: ActivityResultCaller,
        context: Context
    ) =
        caller.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.containsValue(false)) {
                // The user chose to deny one or more permissions
                Toast.makeText(
                    context, "At least one permission was not granted", Toast.LENGTH_SHORT
                ).show()
            } else {
                // The user chose to grant all permissions
                useFeature()
            }
        }
}