plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("de.mannodermaus.android-junit5")
}

@Suppress("UnstableApiUsage")
android {
    namespace = "com.example.flickr.ui.home"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Version.KOTLIN_COMPILER_EXTENSION_VERSION
    }
}

dependencies {
    androidxCore()
    compose()
    composeNavigation()
    composeViewModel()
    composeMaterial()
    coroutines()
    hilt()
    coilCompose()
    placeholder()
    gson()

    junit5()
    junit4()
    androidXTest()
    espresso()
    mockito()
    composeTest()
    hiltTest()

    moduleDependency(projects.ui.common)
    moduleDependency(projects.ui.search)
    moduleDependency(projects.ui.detail)
    moduleDependency(projects.data.common)
    moduleDependency(projects.data.bookmark)
}