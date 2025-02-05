package com.startup.common.util

import android.Manifest

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

    fun getTypeOfPermission(type: Permission): Array<String> {
        return when (type) {
            Permission.GALLERY -> PERMISSION_GALLERY
            Permission.FILE -> PERMISSION_FILE
            Permission.CAMERA -> PERMISSION_CAMERA
            Permission.POST_NOTIFICATIONS -> PERMISSION_NOTIFICATION
            Permission.RECORD_AUDIO -> PERMISSION_RECORD_AUDIO
        }
    }

    enum class Permission {
        GALLERY,
        CAMERA,
        FILE,
        RECORD_AUDIO,
        POST_NOTIFICATIONS
    }
}
