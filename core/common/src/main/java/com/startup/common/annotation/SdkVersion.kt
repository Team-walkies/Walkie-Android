package com.startup.common.annotation

import android.os.Build
import androidx.annotation.IntDef

@IntDef(
    Build.VERSION_CODES.N,
    Build.VERSION_CODES.N_MR1,
    Build.VERSION_CODES.O,
    Build.VERSION_CODES.O_MR1,
    Build.VERSION_CODES.P,
    Build.VERSION_CODES.Q,
    Build.VERSION_CODES.R,
    Build.VERSION_CODES.S,
    Build.VERSION_CODES.S_V2,
    Build.VERSION_CODES.TIRAMISU,
    Build.VERSION_CODES.UPSIDE_DOWN_CAKE,
)
@Retention(AnnotationRetention.SOURCE)
internal annotation class SdkVersion
