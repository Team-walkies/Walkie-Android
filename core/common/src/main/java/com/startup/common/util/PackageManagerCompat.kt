package com.startup.common.util

import android.content.pm.PackageInfo
import android.content.pm.PackageManager

private fun PackageManager.packageInfo(packageName: String, flags: Int = 0): PackageInfo {
    return if (OsVersions.isGreaterThanOrEqualsTIRAMISU()) {
        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
    } else {
        getPackageInfo(packageName, flags)
    }
}

fun PackageManager.getLongVersionCode(packageName: String, flags: Int = 0): Long {
    val packageInfo = packageInfo(packageName, flags)
    return if (OsVersions.isGreaterThanOrEqualsP()) {
        packageInfo.longVersionCode
    } else {
        @Suppress("DEPRECATION")
        packageInfo.versionCode.toLong()
    }
}