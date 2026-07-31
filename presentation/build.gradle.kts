import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.picke.presentation"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    val properties = Properties()
    val propertiesFile = project.rootProject.file("local.properties")
    if (propertiesFile.exists()) {
        properties.load(propertiesFile.inputStream())
    }

    // UI 계층에서 필요한 키값만 추출
    val baseUrlDebug = properties.getProperty("BASE_URL_DEBUG") ?: ""
    val baseUrlRelease = properties.getProperty("BASE_URL_RELEASE") ?: ""

    val kakaoDebugAppKey = properties.getProperty("KAKAO_DEBUG_APPKEY") ?: ""
    val admobRewardedAdUnitId = properties.getProperty("ADMOB_REWARDED_AD_UNIT_ID") ?: ""
    val mixpanelToken = properties.getProperty("MIXPANEL_PROJECT_TOKEN") ?: ""
    val adfitBanner320x50 = properties.getProperty("ADFIT_BANNER_320X50") ?: ""
    val adfitBanner320x100 = properties.getProperty("ADFIT_BANNER_320X100") ?: ""
    val adfitBanner320x480 = properties.getProperty("ADFIT_BANNER_320X480") ?: ""
    val adfitNative2x1 = properties.getProperty("ADFIT_NATIVE_2X1") ?: ""
    val adfitNative1x1 = properties.getProperty("ADFIT_NATIVE_1X1") ?: ""
    val adfitAppTransition = properties.getProperty("ADFIT_APP_TRANSITION") ?: ""
    val googleWebClientId = properties.getProperty("GOOGLE_WEB_CLIENT_ID") ?: ""

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        buildConfigField("String", "KAKAO_DEBUG_APPKEY", "\"$kakaoDebugAppKey\"")
        buildConfigField("String", "ADMOB_REWARDED_AD_UNIT_ID", "\"$admobRewardedAdUnitId\"")
        buildConfigField("String", "MIXPANEL_PROJECT_TOKEN", "\"$mixpanelToken\"")
        buildConfigField("String", "ADFIT_BANNER_320X50", "\"$adfitBanner320x50\"")
        buildConfigField("String", "ADFIT_BANNER_320X100", "\"$adfitBanner320x100\"")
        buildConfigField("String", "ADFIT_BANNER_320X480", "\"$adfitBanner320x480\"")
        buildConfigField("String", "ADFIT_NATIVE_2X1", "\"$adfitNative2x1\"")
        buildConfigField("String", "ADFIT_NATIVE_1X1", "\"$adfitNative1x1\"")
        buildConfigField("String", "ADFIT_APP_TRANSITION", "\"$adfitAppTransition\"")
        // 배포용 구글 클라이언트 ID 설정
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleWebClientId\"")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    buildTypes {
        release {
            // 배포용 서버 주소 설정
            buildConfigField("String", "BASE_URL", "\"$baseUrlDebug\"")
        }
        debug {
            // 개발용 서버 주소 설정
            buildConfigField("String", "BASE_URL", "\"$baseUrlRelease\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":domain"))

    // [Compose & UI]
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // [Navigation]
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // [DI - Hilt]
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Coil
    implementation(libs.coil.compose)

    // [Media & Paging3]
    implementation(libs.androidx.paging.compose)

    // [Social & Ads & Analytics]
    implementation(libs.kakao.sdk.user)
    implementation(libs.kakao.share)
    implementation(libs.google.play.services.auth)
    implementation(libs.play.services.ads)
    implementation(libs.kakao.adfit)
    implementation(libs.mixpanel.android)

    // [Firebase] App 레벨 초기화 및 서비스
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database)
    implementation(libs.firebase.dynamic.links)
    implementation(libs.firebase.messaging)

    // Splash
    implementation(libs.androidx.core.splashscreen)

    // Exoplayer
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.datasource.okhttp)
}