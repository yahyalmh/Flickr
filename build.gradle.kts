// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.4.0" apply false
    id("com.android.library") version "7.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.7.21" apply false
    id("org.jetbrains.kotlin.jvm") version "1.7.21" apply false
    id("com.google.dagger.hilt.android") version Version.HILT apply false
    id("de.mannodermaus.android-junit5") version "1.8.2.1" apply false
}