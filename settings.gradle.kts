pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") } // kakao SDK 전용 저장소
        maven { url = uri("https://jitpack.io") } // tosspayments SDK 저장소
    }
}

rootProject.name = "SwypApp"
include(":app")
include(":presentation")
include(":data")
include(":domain")