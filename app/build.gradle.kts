import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    kotlin("kapt")
}

android {
    // [1. 앱의 고유 식별자 및 컴파일 도구
    namespace = "com.picke.app" // 패키지 명칭
    compileSdk = 36 // 앱 빌드할 때 사용할 안드로이드 SDK 버전

    // [2. 앱의 기본 정보]
    defaultConfig {
        applicationId = "com.picke.app" // 플레이 스토어에서 앱을 구분하는 ID
        minSdk = 26 // 앱 실행 가능한 최소 안드로이드 버전
        targetSdk = 35 // 앱 최적화되어 동작할 안드로이드 버전
        versionCode = 1 // 기계가 인식하는 버전, TODO 업데이트 시 무조건 이전보다 커야함
        versionName = "1.0" // 사용자에게 보여질 버전
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // [3. 외부 비밀 키 로드 로직]
        // local.properties 파일에서 읽어오는 과정
        val properties = Properties()
        val propertiesFile = project.rootProject.file("local.properties")
        if (propertiesFile.exists()) {
            properties.load(propertiesFile.inputStream())
        }

        val kakaoDebugAppKey = properties.getProperty("KAKAO_DEBUG_APPKEY") ?: ""
        val googleWebClientId = properties.getProperty("GOOGLE_WEB_CLIENT_ID") ?: ""
        val admobAppId = properties.getProperty("ADMOB_APP_ID") ?: ""
        val admobRewardedAdUnitId = properties.getProperty("ADMOB_REWARDED_AD_UNIT_ID") ?: ""
        println("🔑💛 KAKAO_DEBUG_APPKEY: $kakaoDebugAppKey")
        println("🔑💚 GOOGLE_WEB_CLIENT_ID: $googleWebClientId")
        println("🔑🤍 ADMOB_APP_ID: $admobAppId")
        println("🔑🤍 ADMOB_REWARDED_AD_UNIT_ID: $admobRewardedAdUnitId")

        // [4. 코드 및 매니페스트로 값 전달]
        buildConfigField("String", "KAKAO_DEBUG_APPKEY", "\"$kakaoDebugAppKey\"")
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleWebClientId\"")
        buildConfigField("String", "ADMOB_REWARDED_AD_UNIT_ID", "\"$admobRewardedAdUnitId\"")

        manifestPlaceholders["admobAppId"] = admobAppId
        manifestPlaceholders["kakaoDebugAppKey"] = kakaoDebugAppKey
    }

    buildTypes {
        // [5. 배포용 빌드 설정]
        release {
            isMinifyEnabled = true // 코드 난독화 및 최적화
            isShrinkResources = true // 미사용 이미지, 레이아웃 리소스 삭제하여 용량 줄이기

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // 배포용 서버 주소 설정
            buildConfigField("String", "BASE_URL", "\"https://picke.store/\"")
        }
        // [6. 개발용 빌드 설정]
        debug {
            isMinifyEnabled = false
            // 개발용 서버 주소 설정
            buildConfigField("String", "BASE_URL", "\"https://dev.picke.store/\"")
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
        jvmTarget = "17" // 코틀린 코드를 jvm 17 타겟으로 변환
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
}

configurations.all {
    resolutionStrategy {
        force("androidx.compose.foundation:foundation:1.7.4")
        force("androidx.compose.foundation:foundation-layout:1.7.4")
    }
}