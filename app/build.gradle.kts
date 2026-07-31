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
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    val properties = Properties()
    val propertiesFile = project.rootProject.file("local.properties")
    if (propertiesFile.exists()) {
        properties.load(propertiesFile.inputStream())
    }

    val kakaoDebugAppKey = properties.getProperty("KAKAO_DEBUG_APPKEY") ?: ""
    val admobAppId = properties.getProperty("ADMOB_APP_ID") ?: ""
    val sentryDsn = properties.getProperty("SENTRY_DSN") ?: ""

    defaultConfig {
        applicationId = "com.picke.app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 22
        versionName = "1.1.7"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 앱 레벨 Manifest에 주입할 값들
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
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "KAKAO_DEBUG_APPKEY", "\"$kakaoDebugAppKey\"")
            // Sentry 환경 구분 (배포)
            manifestPlaceholders["sentryEnvironment"] = "production"
            manifestPlaceholders["sentryDebug"] = "false"
        }
        // [6. 개발용 빌드 설정]
        debug {
            isMinifyEnabled = false
            buildConfigField("String", "KAKAO_DEBUG_APPKEY", "\"$kakaoDebugAppKey\"")
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
    kotlinOptions {
        jvmTarget = "17"
    }
}

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
    // 💡 모든 하위 모듈 조립
    implementation(project(":presentation"))
    implementation(project(":data"))
    implementation(project(":domain"))

    // [Android Core & App Entry]
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // [DI - Hilt] Root 컴포넌트 생성용
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // [Firebase] App 레벨 초기화 및 서비스
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database)
    implementation(libs.firebase.dynamic.links)
    implementation(libs.firebase.messaging)

    // ProcessLifecycleOwner
    implementation(libs.androidx.lifecycle.process)

    // KakaoSdk
    implementation(libs.kakao.sdk.user) // 또는 libs.kakao.sdk.user
}

configurations.all {
    resolutionStrategy {
        force("androidx.compose.foundation:foundation:1.7.4")
        force("androidx.compose.foundation:foundation-layout:1.7.4")
    }
}