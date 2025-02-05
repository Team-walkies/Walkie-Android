package com.startup.common.util

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import com.startup.common.annotation.SdkVersion

object OsVersions {

    private val currentSdkVersion = Build.VERSION.SDK_INT

    @ChecksSdkIntAtLeast(parameter = 0)
    private fun isGreaterThanOrEquals(@SdkVersion sdkVersion: Int) = currentSdkVersion >= sdkVersion
    private fun isEquals(@SdkVersion sdkVersion: Int) = currentSdkVersion == sdkVersion

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
    fun isGreaterThanOrEqualsN() = isGreaterThanOrEquals(Build.VERSION_CODES.N)
    fun isEqualsN() = isEquals(Build.VERSION_CODES.N)

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N_MR1)
    fun isGreaterThanOrEqualsN_MR1() = isGreaterThanOrEquals(Build.VERSION_CODES.N_MR1)
    fun isEqualsN_MR1() = isEquals(Build.VERSION_CODES.N_MR1)

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    fun isGreaterThanOrEqualsO() = isGreaterThanOrEquals(Build.VERSION_CODES.O)
    fun isEqualsO() = isEquals(Build.VERSION_CODES.O)

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O_MR1)
    fun isGreaterThanOrEqualsO_MR1() = isGreaterThanOrEquals(Build.VERSION_CODES.O_MR1)
    fun isEqualsO_MR1() = isEquals(Build.VERSION_CODES.O_MR1)

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
    fun isGreaterThanOrEqualsP() = isGreaterThanOrEquals(Build.VERSION_CODES.P)
    fun isEqualsP() = isEquals(Build.VERSION_CODES.P)

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    fun isGreaterThanOrEqualsQ() = isGreaterThanOrEquals(Build.VERSION_CODES.Q)
    fun isEqualsQ() = isEquals(Build.VERSION_CODES.Q)

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
    fun isGreaterThanOrEqualsR() = isGreaterThanOrEquals(Build.VERSION_CODES.R)
    fun isEqualsR() = isEquals(Build.VERSION_CODES.R)

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    fun isGreaterThanOrEqualsS() = isGreaterThanOrEquals(Build.VERSION_CODES.S)
    fun isEqualsS() = isEquals(Build.VERSION_CODES.S)

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S_V2)
    fun isGreaterThanOrEqualsS_V2() = isGreaterThanOrEquals(Build.VERSION_CODES.S_V2)
    fun isEqualsS_V2() = isEquals(Build.VERSION_CODES.S_V2)

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    fun isGreaterThanOrEqualsTIRAMISU() = isGreaterThanOrEquals(Build.VERSION_CODES.TIRAMISU)
    fun isEqualsTIRAMISU() = isEquals(Build.VERSION_CODES.TIRAMISU)

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun isGreaterThanOrEqualsUPSIDEDOWNCAKE() = isGreaterThanOrEquals(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun isEqualsUPSIDEDOWNCAKE() = isEquals(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun isGreaterThanOrEqualsVANILLA_ICE_CREAM() = isGreaterThanOrEquals(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun isEqualsVANILLA_ICE_CREAM() = isEquals(Build.VERSION_CODES.VANILLA_ICE_CREAM)

}
