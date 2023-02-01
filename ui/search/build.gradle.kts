plugins {
    androidLibrary()
    kotlinAndroid()
    kapt()
    hilt()
    junit5Plugin()
}

@Suppress("UnstableApiUsage")
android {
    namespace = "com.example.flickr.ui.search"
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
    compose()
    composeNavigation()
    composeViewModel()
    composeMaterial()
    placeholder()
    coilCompose()
    hilt()

    junit5()
    junit4()
    androidXTest()
    espresso()
    mockito()
    composeTest()
    hiltTest()

    moduleDependency(projects.data.common)
    moduleDependency(projects.data.search)
    moduleDependency(projects.data.bookmark)
    moduleDependency(projects.data.searchhistory)
    moduleDependency(projects.ui.common)
    moduleDependency(projects.ui.detail)
}