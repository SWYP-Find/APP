import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    kotlin("kapt")
}

android {
    // [1. 앱의 고유 식별자 및 컴파일 도구]
    namespace = "com.picke.app"
    compileSdk = 36

    val properties = Properties()
    val propertiesFile = project.rootProject.file("local.properties")
    if (propertiesFile.exists()) {
        properties.load(propertiesFile.inputStream())
    }

    val kakaoDebugAppKey = properties.getProperty("KAKAO_DEBUG_APPKEY") ?: ""
    val googleWebClientId = properties.getProperty("GOOGLE_WEB_CLIENT_ID") ?: ""
    val googleWebClientIdDebug = properties.getProperty("GOOGLE_WEB_CLIENT_ID_DEBUG") ?: ""
    val googleWebClientIdRelease = properties.getProperty("GOOGLE_WEB_CLIENT_ID_RELEASE") ?: ""
    val admobAppId = properties.getProperty("ADMOB_APP_ID") ?: ""
    val admobRewardedAdUnitId = properties.getProperty("ADMOB_REWARDED_AD_UNIT_ID") ?: ""
    val mixpanelToken = properties.getProperty("MIXPANEL_PROJECT_TOKEN") ?: ""

    println("🔑💛 KAKAO_DEBUG_APPKEY: $kakaoDebugAppKey")
    println("🔑💚 GOOGLE_WEB_CLIENT_ID_DEBUG: $googleWebClientIdDebug")
    println("🔑💚 GOOGLE_WEB_CLIENT_ID_RELEASE: $googleWebClientIdRelease")
    println("🔑🤍 ADMOB_APP_ID: $admobAppId")
    println("🔑🤍 ADMOB_REWARDED_AD_UNIT_ID: $admobRewardedAdUnitId")

    // [2. 앱의 기본 정보]
    defaultConfig {
        applicationId = "com.picke.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 2
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // [4. 코드 및 매니페스트로 값 전달]
        buildConfigField("String", "KAKAO_DEBUG_APPKEY", "\"$kakaoDebugAppKey\"")
        buildConfigField("String", "ADMOB_REWARDED_AD_UNIT_ID", "\"$admobRewardedAdUnitId\"")
        buildConfigField("String", "MIXPANEL_PROJECT_TOKEN", "\"$mixpanelToken\"")

        manifestPlaceholders["admobAppId"] = admobAppId
        manifestPlaceholders["kakaoDebugAppKey"] = kakaoDebugAppKey
    }

    buildTypes {
        // [5. 배포용 빌드 설정]
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // 배포용 서버 주소 설정
            buildConfigField("String", "BASE_URL", "\"https://picke.store/\"")
            // 배포용 구글 클라이언트 ID 설정
            buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleWebClientId\"")
        }
        // [6. 개발용 빌드 설정]
        debug {
            isMinifyEnabled = false
            // 개발용 서버 주소 설정
            buildConfigField("String", "BASE_URL", "\"https://picke.store/\"")
            // 개발용 구글 클라이언트 ID 설정
            buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleWebClientId\"")
        }
    }

    // [7. 컴파일러 및 언어 옵션]
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
}

dependencies {
    // [Android Core & Lifecycle] 안드로이드 기본 뼈대 및 생명주기 관리
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)

    // [Compose UI Core] 제트팩 컴포즈 화면 그리기 필수 도구들
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.geometry)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.animation.core)

    // [Navigation] 화면 이동 / 라우팅
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // [DI - Hilt] 의존성 주입
    implementation(libs.hilt.android)
    implementation(libs.androidx.foundation)
    kapt(libs.hilt.compiler)

    // [Network - Retrofit] 서버 API 통신
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

    // [Image & Audio] 미디어 처리
    implementation(libs.coil.compose)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    // [Firebase] 구글 파이어베이스 서비스
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database)
    implementation(libs.firebase.dynamic.links)

    // [Paging3] 무한 스크롤 및 대용량 리스트 페이징 처리
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.common)

    // [Social & Security & Ads] 소셜 로그인, 보안, 광고
    implementation(libs.kakao.sdk.user) // 카카오 로그인
    implementation(libs.kakao.share) // 카카오톡 공유하기
    implementation(libs.google.play.services.auth) // 구글 로그인
    implementation(libs.androidx.security.crypto) // 보안 공유 환경설정(EncryptedSharedPreferences) 등 암호화
    implementation(libs.play.services.ads.api) // 구글 AdMob 광고

    // [SSE] 실시간 통신
    implementation(libs.okhttp.sse)

    // [보류] 로컬 데이터베이스 (Room)
    // implementation(libs.androidx.room.ktx)

    // [Test] 테스트 코드 작성용 도구들
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // [MixPanel]
    implementation(libs.mixpanel.android)
}

configurations.all {
    resolutionStrategy {
        force("androidx.compose.foundation:foundation:1.7.4")
        force("androidx.compose.foundation:foundation-layout:1.7.4")
    }
}