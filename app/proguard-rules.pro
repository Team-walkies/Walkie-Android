# 전역적인 처리

# Compose 전반
-keep class androidx.compose.** { *; }
# Compose Material3 (optional)
-keep class androidx.compose.material3.** { *; }
# Composable 어노테이션을 유지
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
# WebView 콜백을 오버라이드한 경우
-keepclassmembers class * extends android.webkit.WebViewClient {
    public *;
}
-keepclassmembers class * extends android.webkit.WebChromeClient {
    public *;
}

-keepclassmembers class com.startup.spot.SpotBridgeJsInterface {
    public *;
}
-keepclassmembers class com.startup.spot.modify.ModifyReviewBridgeJsInterface {
    public *;
}
-keep class com.startup.spot.modify.ModifyReviewBridgeJsInterface
-keep class com.startup.spot.SpotBridgeJsInterface

-keep public class * extends java.lang.Exception

# @Keep 어노테이션
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# Kotlin Metadata (리플렉션 대비)
-keep @kotlin.Metadata class *

# Coroutine
-keep class kotlinx.coroutines.** { *; }

# KSP + Kotlin Metadata
-keep @kotlin.Metadata class *

# Data Layer
-keep class com.startup.data.di.**_Factory { *; }
-keep class com.startup.data.di.** { *; }
# Retrofit interface
-keep interface com.startup.data.remote.service.** { *; }
# 내부 모델 직렬화용
-keep class com.startup.data.local.entity.** { *; }
-keep class com.startup.data.remote.dto.** { *; }
-keep class com.startup.data.remote.BaseResponse


# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }

-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# With R8 full mode generic signatures are stripped for classes that are not kept.
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# 모든 object를 자동 보호
-keepclassmembers class ** {
    static ** INSTANCE;
}

-keepclassmembers class ** {
    *** get*();
    *** is*();
    *** set*(***);
}

# Gson
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Kotlin Metadata for reflection
-keep @kotlin.Metadata class *
-dontwarn java.lang.invoke.StringConcatFactory

# DataStore: 프로토나 Preferences는 별도 처리가 필요 없음 (R8에 안전)
# DataStore (안전하지만 혹시 몰라 기본 keep)
-keep class androidx.datastore.** { *; }

# Kakao SDK (예: Login)
-keep class com.kakao.** { *; }
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter
-keep interface com.kakao.sdk.**.*Api


# Home
-keep class com.startup.home.navigation.** { *; }
-keep class com.startup.home.di.** { *; }

# Common
# Lottie Animation
-keep class com.airbnb.lottie.** { *; }
-keep class com.startup.common.base.** { *; }
-keep class com.startup.common.provider.** { *; }
-keep class com.startup.common.di.** { *; }
-keep class com.startup.common.util.** { *; }
-keep class com.startup.common.event.** { *; }
-keep class com.startup.common.extension.** { *; }

# Model
-keep class com.startup.model.** { *; }

# GA
-keep class com.startup.ga.** { *; }

# Login
-keep class com.startup.login.navigation.** { *; }
-keep class com.startup.login.di.** { *; }

# Spot
-keep class com.startup.spot.navigation.** { *; }
-keep class com.startup.spot.di.** { *; }

# Step Counter
-keep class com.startup.stepcounter.di.** { *; }
-keep class com.startup.stepcounter.navigation.** { *; }
-keep class com.startup.stepcounter.notification.** { *; }
-keep class com.startup.stepcounter.service.** { *; }
-keep class com.startup.stepcounter.broadcastReciver.** { *; }

# Design-System
-keep class com.startup.design_system.ui.** { *; }
-keep class com.startup.design_system.widget.** { *; }

# ViewModel 키
-keep class **ViewModel_HiltModules$KeyModule { *; }

# Hilt ViewModel
-keep class dagger.hilt.android.lifecycle.HiltViewModel
# Hilt internal 처리용

-keepclassmembers class * {
    @dagger.hilt.android.AndroidEntryPoint *;
    @javax.inject.Inject *;
}
# EntryPoint로 정의된 클래스
-keep interface com.startup.**.*EntryPoint
-keep class com.startup.**.*EntryPoint { *; }
-keep class **_MembersInjector { *; }

# 🔐 Hilt DI 구성요소
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class androidx.hilt.** { *; }
-keep class **_HiltModules* { *; }
-keep class **_GeneratedInjector { *; }
-keep class **_Factory { *; }
-keep class **_HiltComponents* { *; }

# 모든 Hilt EntryPoint 인터페이스 보호 (범용적으로 추가 가능)
-keep class dagger.hilt.internal.GeneratedComponent { *; }
-keep class dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }
-keep class dagger.hilt.android.internal.managers.* { *; }
-keep class dagger.hilt.android.internal.modules.* { *; }
-keep class **.HiltWrapper_* { *; }
-keep @dagger.Module class * { *; }
-keep @dagger.Provides class * { *; }
-keep @dagger.hilt.InstallIn class * { *; }
-keep @dagger.hilt.EntryPoint class * { *; }
-keep class dagger.hilt.EntryPoints { *; }
-keep class dagger.hilt.android.EntryPointAccessors { *; }

# 🔐 ViewModel + EntryPoint 포함된 클래스들
-keep class androidx.lifecycle.ViewModel
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }

# 🔐 DI 대상 생성자
-keepclassmembers class * {
    @javax.inject.Inject <init>(...);
}
-keepclassmembers class * {
    @dagger.Provides *;
}
# 📌 reflection 대비 필수 메타데이터
-keep @kotlin.Metadata class *
-keepattributes *Annotation*
-keepattributes Signature
