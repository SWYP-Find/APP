import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    kotlin("kapt")
}

android {
    namespace = "com.picke.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.picke.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        val propertiesFile = project.rootProject.file("local.properties")
        if (propertiesFile.exists()) {
            properties.load(propertiesFile.inputStream())
        }

        val kakaoKey = properties.getProperty("KAKAO_DEBUG_APPKEY") ?: ""
        val googleClientId = properties.getProperty("GOOGLE_WEB_CLIENT_ID") ?: ""
        val admobAppId = properties.getProperty("ADMOB_APP_ID") ?: ""
        val admobRewardedAdUnitId = properties.getProperty("ADMOB_REWARDED_AD_UNIT_ID") ?: ""

        buildConfigField("String", "KAKAO_DEBUG_APPKEY", "\"$kakaoKey\"")
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleClientId\"")
        manifestPlaceholders["admobAppId"] = admobAppId
        manifestPlaceholders["kakaoDebugAppKey"] = kakaoKey
        buildConfigField("String", "ADMOB_REWARDED_AD_UNIT_ID", "\"$admobRewardedAdUnitId\"")
    }

    buildTypes {
        release {
            // ✨ 1. 코드를 난독화하고 사용하지 않는 코드를 제거합니다. (로그 지우기의 기본 설정)
            isMinifyEnabled = true

            // ✨ 2. 사용하지 않는 이미지나 리소스 파일도 함께 제거해서 앱 용량을 획기적으로 줄여줍니다.
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // 배포용 서버 주소
            buildConfigField("String", "BASE_URL", "\"https://picke.store/\"")
        }
        debug {
            // 개발용 서버 주소
            buildConfigField("String", "BASE_URL", "\"https://picke.store/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    kotlinOptions{
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation("androidx.core:core-splashscreen:1.0.1")

    // [DI - Hilt]
    implementation(libs.hilt.android)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.ui.geometry)
    implementation(libs.androidx.benchmark.traceprocessor)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.play.services.ads.api)
    implementation(libs.foundation)
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.animation.core)
    implementation(libs.androidx.room.ktx)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // [Network - Retrofit]
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

    // [Image & Audio]
    implementation(libs.coil.compose)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    // [Firebase]
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database)
    implementation(libs.firebase.dynamic.links)

    // Paging3
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // [Social & Security & Payment]
    implementation(libs.kakao.sdk.user)
    implementation(libs.kakao.share)
    implementation(libs.androidx.security.crypto)
    implementation(libs.google.play.services.auth)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}