import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec

object Plugins {
    const val ANDROID_APPLICATION = "com.android.application"
    const val ANDROID_LIBRARAY = "com.android.library"
    const val KOTLIN_ANDROID = "org.jetbrains.kotlin.android"
    const val KOTLIN_JVM = "org.jetbrains.kotlin.jvm"
    const val HILT_PLUGIN = "com.google.dagger.hilt.android"
    const val JUNIT5_GRADLE_PLUGIN = "de.mannodermaus.android-junit5"
    const val GRADLE_VERSIONS = "com.github.ben-manes.versions"
}

fun PluginDependenciesSpec.androidApplication() =
    id(Plugins.ANDROID_APPLICATION) version Version.Plugin.Android.APPLICATION

fun PluginDependenciesSpec.androidLibrary() =
    id(Plugins.ANDROID_LIBRARAY) version Version.Plugin.Android.LIBRARY

fun PluginDependenciesSpec.kapt() = kotlin("kapt")

fun PluginDependenciesSpec.kotlinAndroid() =
    id(Plugins.KOTLIN_ANDROID) version Version.Plugin.Kotlin.ANDROID

fun PluginDependenciesSpec.kotlinJvm() =
    id(Plugins.KOTLIN_JVM) version Version.Plugin.Kotlin.JVM

fun PluginDependenciesSpec.hilt() =
    id(Plugins.HILT_PLUGIN) version Version.HILT

fun PluginDependenciesSpec.junit5Plugin() =
    id(Plugins.JUNIT5_GRADLE_PLUGIN) version Version.Plugin.JUNIT5_GRADLE_PLUGIN

fun PluginDependenciesSpec.gradleVersions() =
    id(Plugins.GRADLE_VERSIONS) version Version.Plugin.GRADLE_VERSIONS
