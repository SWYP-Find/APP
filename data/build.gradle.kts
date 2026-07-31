import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.picke.data"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    val properties = Properties()
    val propertiesFile = project.rootProject.file("local.properties")
    if (propertiesFile.exists()) {
        properties.load(propertiesFile.inputStream())
    }

    val mixpanelToken = properties.getProperty("MIXPANEL_PROJECT_TOKEN") ?: ""

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        buildConfigField("String", "MIXPANEL_PROJECT_TOKEN", "\"$mixpanelToken\"")
    }

    buildTypes {
        release {
            buildConfigField("String", "BASE_URL", "\"https://picke.store/\"")
        }
        debug {
            buildConfigField("String", "BASE_URL", "\"https://dev.picke.store/\"")
        }
    }

    buildFeatures {
        buildConfig = true
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

    // [DI - Hilt]
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // [Network - Retrofit]
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

    // [Security]
    implementation(libs.androidx.security.crypto)

    // [Paging3]
    implementation(libs.androidx.paging.runtime)

    // 💡 믹스패널 라이브러리 추가
    implementation(libs.mixpanel.android)
}