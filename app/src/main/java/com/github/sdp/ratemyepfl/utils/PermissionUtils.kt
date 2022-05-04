package com.github.sdp.ratemyepfl.utils

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResultCaller
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
     * Checks whether [permission] is granted in the current [Context]
     */
    fun hasPermission(permission: String, context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    /**
     * Checks whether all [permissions] are granted in the current [Context]
     */
    fun hasAllPermissions(permissions: Array<String>, context: Context): Boolean =
        permissions.all { hasPermission(it, context) }

    /**
     * Starts a feature of the phone that requires a permission to be used
     * (e.g., location, camera)
     *
     * @param useFeature The function that uses the feature
     * @param permission The permission that needs be to granted by the user
     * @param caller The [Fragment]/[Activity] from which the feature is started
     * @param context The current context
     */
    fun startPhoneFeature(
        useFeature: () -> Unit,
        permission: String,
        caller: ActivityResultCaller,
        context: Context
    ) {
        if (hasPermission(permission, context)) {
            // The permission was already granted
            useFeature()
        } else {
            // Request the user to grant the permission at runtime
            requestPermissionLauncher(useFeature, caller, context).launch(permission)
        }
    }

    /**
     * Starts features of the phone that requires multiple permissions to be used
     * (e.g., gallery read/write)
     *
     * @param useFeature The function that uses the feature
     * @param permission The permissions that need be to granted by the user
     * @param caller The [Fragment]/[Activity] from which the features are started
     * @param context The current context
     */
    fun startPhoneFeatures(
        useFeatures: () -> Unit,
        permissions: Array<String>,
        caller: ActivityResultCaller,
        context: Context
    ) {
        if (hasAllPermissions(permissions, context)) {
            // The permissions were already granted
            useFeatures()
        } else {
            // Request the user to grant the permissions at runtime
            requestMultiplePermissionsLauncher(useFeatures, caller, context).launch(permissions)
        }
    }

    /**
     * Checks the user's response, whether they chose to grant or deny a runtime permission
     *
     * @param useFeature The function that uses the feature
     * @param caller The [Fragment]/[Activity] from which the feature is started
     * @param context The current context
     */
    private fun requestPermissionLauncher(
        useFeature: () -> Unit,
        caller: ActivityResultCaller,
        context: Context
    ) =
        caller.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // The user chose to grant the permission
                useFeature()
            } else {
                // The user chose to deny the permission
                Toast.makeText(
                    context, "Permission was not granted", Toast.LENGTH_SHORT
                ).show()
            }
        }

    /**
     * Checks the user's response, whether they chose to grant or deny runtime permissions
     *
     * @param useFeatures The function that uses the features
     * @param caller The [Fragment]/[Activity] from which the feature is started
     * @param context The current context
     */
    private fun requestMultiplePermissionsLauncher(
        useFeatures: () -> Unit,
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
                useFeatures()
            }
        }
}