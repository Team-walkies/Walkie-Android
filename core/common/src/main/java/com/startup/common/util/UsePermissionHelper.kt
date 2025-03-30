package com.startup.common.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat

object UsePermissionHelper {
    private val PERMISSION_CAMERA = if (OsVersions.isGreaterThanOrEqualsTIRAMISU()) {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES,
        )
    } else if (OsVersions.isGreaterThanOrEqualsR()) {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    } else {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
    private val PERMISSION_GALLERY = if (OsVersions.isGreaterThanOrEqualsUPSIDEDOWNCAKE()) {
        arrayOf(
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
        )
    } else if (OsVersions.isGreaterThanOrEqualsTIRAMISU()) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
        )
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val PERMISSION_FILE = if (OsVersions.isGreaterThanOrEqualsTIRAMISU()) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO
        )
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val PERMISSION_NOTIFICATION = if (OsVersions.isGreaterThanOrEqualsTIRAMISU()) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        arrayOf()
    }

    private val PERMISSION_RECORD_AUDIO = arrayOf(Manifest.permission.RECORD_AUDIO)

    private val PERMISSION_ACTIVITY_RECOGNITION = if (OsVersions.isGreaterThanOrEqualsQ()) {
        arrayOf(Manifest.permission.ACTIVITY_RECOGNITION)
    } else {
        arrayOf()
    }

    private val PERMISSION_BATTERY_OPTIMIZATION =
        arrayOf(Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)

    private val FOREGROUND_LOCATION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val BACKGROUND_LOCATION = if (OsVersions.isGreaterThanOrEqualsQ()) {
        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    } else {
        arrayOf()
    }

    fun getTypeOfPermission(type: Permission): Array<String> {
        return when (type) {
            Permission.GALLERY -> PERMISSION_GALLERY
            Permission.FILE -> PERMISSION_FILE
            Permission.CAMERA -> PERMISSION_CAMERA
            Permission.POST_NOTIFICATIONS -> PERMISSION_NOTIFICATION
            Permission.RECORD_AUDIO -> PERMISSION_RECORD_AUDIO
            Permission.ACTIVITY_RECOGNITION -> PERMISSION_ACTIVITY_RECOGNITION
            Permission.BATTERY_OPTIMIZATION -> PERMISSION_BATTERY_OPTIMIZATION
            Permission.FOREGROUND_LOCATION -> FOREGROUND_LOCATION
            Permission.BACKGROUND_LOCATION -> TODO()
        }
    }

    fun isGrantedPermissions(context: Context, vararg permissions: String): Boolean {
        if (permissions.isNotEmpty()) {
            permissions.forEach {
                if (ContextCompat.checkSelfPermission(
                        context,
                        it
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    return false
                }
            }
            return true
        } else {
            return false
        }
    }

    fun getPermissionSettingsIntent(context: Context): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    enum class Permission {
        GALLERY,
        CAMERA,
        FILE,
        RECORD_AUDIO,
        POST_NOTIFICATIONS,
        ACTIVITY_RECOGNITION,
        BATTERY_OPTIMIZATION,
        FOREGROUND_LOCATION,
        BACKGROUND_LOCATION
    }
}