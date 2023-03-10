plugins {
    androidLibrary()
    kotlinAndroid()
    kapt()
    hilt()
}

@Suppress("UnstableApiUsage")
android {
    namespace = "com.example.flickr.ui.main"
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
    coilCompose()

    junit4()
    composeTest()
    hilt()
    hiltTest()

    moduleDependency(projects.ui.common)
    moduleDependency(projects.ui.home)
    moduleDependency(projects.ui.search)
    moduleDependency(projects.ui.detail)
}