import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sentry.android)
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
    val admobAppId = properties.getProperty("ADMOB_APP_ID") ?: ""
    val admobRewardedAdUnitId = properties.getProperty("ADMOB_REWARDED_AD_UNIT_ID") ?: ""
    val mixpanelToken = properties.getProperty("MIXPANEL_PROJECT_TOKEN") ?: ""
    val sentryDsn = properties.getProperty("SENTRY_DSN") ?: ""
    val adfitBanner320x50 = properties.getProperty("ADFIT_BANNER_320X50") ?: ""
    val adfitBanner320x100 = properties.getProperty("ADFIT_BANNER_320X100") ?: ""
    val adfitBanner320x480 = properties.getProperty("ADFIT_BANNER_320X480") ?: ""
    val adfitNative2x1 = properties.getProperty("ADFIT_NATIVE_2X1") ?: ""
    val adfitNative1x1 = properties.getProperty("ADFIT_NATIVE_1X1") ?: ""
    val adfitAppTransition = properties.getProperty("ADFIT_APP_TRANSITION") ?: ""

    println("🔑💛 KAKAO_DEBUG_APPKEY: $kakaoDebugAppKey")
    println("🔑🤍 GOOGLE_WEB_CLIENT_ID: ${if (googleWebClientId.isNotEmpty()) "${googleWebClientId.take(20)}..." else "❌ 미설정 (local.properties 확인)"}")
    println("🔑🤍 ADMOB_APP_ID: $admobAppId")
    println("🔑🤍 ADMOB_REWARDED_AD_UNIT_ID: $admobRewardedAdUnitId")
    println("🔑🤍 ADFIT_BANNER_320X50: ${if (adfitBanner320x50.isNotEmpty()) adfitBanner320x50 else "❌ 미설정 (local.properties 확인)"}")
    println("🔑🤍 ADFIT_BANNER_320X100: ${if (adfitBanner320x100.isNotEmpty()) adfitBanner320x100 else "❌ 미설정 (local.properties 확인)"}")
    println("🔑🤍 ADFIT_BANNER_320X480: ${if (adfitBanner320x480.isNotEmpty()) adfitBanner320x480 else "❌ 미설정 (local.properties 확인)"}")
    println("🔑🤍 ADFIT_NATIVE_2X1: ${if (adfitNative2x1.isNotEmpty()) adfitNative2x1 else "❌ 미설정 (local.properties 확인)"}")
    println("🔑🤍 ADFIT_NATIVE_1X1: ${if (adfitNative1x1.isNotEmpty()) adfitNative1x1 else "❌ 미설정 (local.properties 확인)"}")
    println("🔑🤍 ADFIT_APP_TRANSITION: ${if (adfitAppTransition.isNotEmpty()) adfitAppTransition else "❌ 미설정 (local.properties 확인)"}")

    // [2. 앱의 기본 정보]
    defaultConfig {
        applicationId = "com.picke.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 31
        versionName = "1.1.9"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // [4. 코드 및 매니페스트로 값 전달]
        buildConfigField("String", "KAKAO_DEBUG_APPKEY", "\"$kakaoDebugAppKey\"")
        buildConfigField("String", "ADMOB_REWARDED_AD_UNIT_ID", "\"$admobRewardedAdUnitId\"")
        buildConfigField("String", "MIXPANEL_PROJECT_TOKEN", "\"$mixpanelToken\"")
        buildConfigField("String", "ADFIT_BANNER_320X50", "\"$adfitBanner320x50\"")
        buildConfigField("String", "ADFIT_BANNER_320X100", "\"$adfitBanner320x100\"")
        buildConfigField("String", "ADFIT_BANNER_320X480", "\"$adfitBanner320x480\"")
        buildConfigField("String", "ADFIT_NATIVE_2X1", "\"$adfitNative2x1\"")
        buildConfigField("String", "ADFIT_NATIVE_1X1", "\"$adfitNative1x1\"")
        buildConfigField("String", "ADFIT_APP_TRANSITION", "\"$adfitAppTransition\"")

        manifestPlaceholders["admobAppId"] = admobAppId
        manifestPlaceholders["kakaoDebugAppKey"] = kakaoDebugAppKey
        manifestPlaceholders["sentryDsn"] = sentryDsn
    }

    signingConfigs {
        create("release") {
            val storeFilePath = properties.getProperty("storeFile")
            if (storeFilePath != null) storeFile = file(storeFilePath)
            storePassword = properties.getProperty("storePassword") ?: ""
            keyAlias = properties.getProperty("keyAlias") ?: ""
            keyPassword = properties.getProperty("keyPassword") ?: ""
        }
    }

    buildTypes {
        // [5. 배포용 빌드 설정]
        release {
            signingConfig = signingConfigs.getByName("release")
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
            // Sentry 환경 구분 (배포)
            manifestPlaceholders["sentryEnvironment"] = "production"
            manifestPlaceholders["sentryDebug"] = "false"
        }
        // [6. 개발용 빌드 설정]
        debug {
            isMinifyEnabled = false
            // 개발용 서버 주소 설정
            buildConfigField("String", "BASE_URL", "\"https://dev.picke.store/\"")
            // 개발용 구글 클라이언트 ID 설정
            buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleWebClientId\"")
            // Sentry 환경 구분 (개발)
            manifestPlaceholders["sentryEnvironment"] = "debug"
            manifestPlaceholders["sentryDebug"] = "true"
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

// [Sentry] 에러 모니터링 설정 (SDK 의존성은 플러그인이 자동 추가)
sentry {
    org.set("picke")
    projectName.set("picke-android")
    // 릴리즈 빌드 시 ProGuard 매핑을 업로드해 난독화된 스택트레이스를 복원.
    // 인증 토큰은 루트의 sentry.properties(gitignore 대상) 또는 SENTRY_AUTH_TOKEN 환경변수에서 읽음.
    includeProguardMapping.set(true)
    autoUploadProguardMapping.set(
        rootProject.file("sentry.properties").exists() || System.getenv("SENTRY_AUTH_TOKEN") != null
    )
    // 소스 코드 업로드는 사용하지 않음 (필요 시 활성화)
    includeSourceContext.set(false)
}


dependencies {
    // [Android Core & Lifecycle] 안드로이드 기본 뼈대 및 생명주기 관리
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.process)
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
    ksp(libs.hilt.compiler)

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
    implementation(libs.firebase.messaging)

    // [Paging3] 무한 스크롤 및 대용량 리스트 페이징 처리
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.common)

    // [Social & Security & Ads] 소셜 로그인, 보안, 광고
    implementation(libs.kakao.sdk.user) // 카카오 로그인
    implementation(libs.kakao.share) // 카카오톡 공유하기
    implementation(libs.google.play.services.auth) // 구글 로그인
    implementation(libs.androidx.security.crypto) // 보안 공유 환경설정(EncryptedSharedPreferences) 등 암호화
    implementation(libs.play.services.ads) // 구글 AdMob 광고
    implementation(libs.kakao.adfit) // 카카오 애드핏 배너 광고
    implementation(libs.play.services.ads.identifier) // 애드핏 SDK 필수 의존성

    // [보류] 로컬 데이터베이스 (Room)
    // implementation(libs.androidx.room.ktx)

    // [Test] 테스트 코드 작성용 도구들
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
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