plugins {
    androidLibrary()
    kotlinAndroid()
    kapt()
    hilt()
    junit5Plugin()
}

@Suppress("UnstableApiUsage")
android {
    namespace = "com.example.flickr.ui.common"
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
    junit4()
    junit5()
    mockito()
    compose()
    hiltTest()
    hilt()
    coilCompose()
    placeholder()
    coroutines()
    composeTest()
    androidXTest()
    composeMaterial()
    moduleDependency(projects.data.common)
}