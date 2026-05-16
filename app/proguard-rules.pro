# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 1. Hilt / Dagger 관련 난독화 방어
-keep class * extends org.checkerframework.checker.nullness.qual.MonotonicNonNull
-keep class dagger.hilt.android.internal.managers.** { *; }
-keep class * implements dagger.hilt.internal.GeneratedEntryPoint
-keep class * implements dagger.hilt.internal.UnsafeCancellableContinuationCallback
-keep @dagger.hilt.android.scopes.** class *

# 2. Retrofit 관련 난독화 방어
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Google Play Services & Auth
-keep class com.google.android.gms.auth.** { *; }
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.tasks.** { *; }

# Firebase (특히 Realtime Database 사용 시 필수)
-keep class com.google.firebase.** { *; }
# AndroidX Splash Screen
-keep class androidx.core.splashscreen.** { *; }
# 3. Gson 관련 난독화 방어
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
# Kakao SDK
-keep class com.kakao.sdk.** { *; }

# ✨ 4. [가장 중요] Pické 앱의 데이터 모델(DTO, Domain) 난독화 방어!
# 패키지명이 com.picke.app이 맞으시죠? 이 안의 데이터 클래스 이름은 절대 바꾸지 말라는 뜻입니다.
-keep class com.picke.app.data.model.** { *; }
-keep class com.picke.app.domain.model.** { *; }

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
    public static int wtf(...);
}

# ==========================================
# Room & WorkManager 예외 처리 (크래시 방지)
# ==========================================
-keep class androidx.work.** { *; }
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.room.RoomDatabase$Builder
-keep @androidx.room.Entity class *
-dontwarn androidx.work.**
-dontwarn androidx.room.**