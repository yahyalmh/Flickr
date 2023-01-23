plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.flickr"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "com.example.flickr"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

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

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,LICENSE}"
            pickFirsts += "/META-INF/{AL2.0,LGPL2.1,LICENSE*}"
        }
    }
}


dependencies {

    androidxCore()
    compose()
    composeMaterial()
    composeNavigation()
    gson()
    coroutines()
    hilt()
    junit4()
    moduleDependency(":ui:main")
}
