plugins {
    androidLibrary()
    kotlinAndroid()
    kapt()
    hilt()
    junit5Plugin()
}
@Suppress("UnstableApiUsage")
android {
    namespace = "com.example.flickr.data.flickr"
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
}

dependencies {
    retrofit()
    coroutines()
    hilt()
    junit5()
    androidXTest()
    espresso()
    mockito()
    moduleDependency(projects.data.common)
}

