import Dependencies.JUNIT_KTX
import Dependencies.ROOM_COMPILER
import Dependencies.ROOM_COROUTINE
import Dependencies.ROOM_KTX
import Dependencies.ROOM_RUNTIME
import Dependencies.ROOM_TEST
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

/**
 * @author yaya (@yahyalmh)
 * @since 28th October 2022
 */

object Dependencies {
    const val ANDROIDX_CORE_KTX = "androidx.core:core-ktx:${Version.Androidx.CORE_KTX}"
    const val ANDROID_LIFECYCLE_RUNTIME =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Version.Androidx.LIFECYCLE}"

    const val COMPOSE_BOM = "androidx.compose:compose-bom:${Version.Compose.BOM}"
    const val ANDROID_ACTIVITY_COMPOSE =
        "androidx.activity:activity-compose:${Version.Compose.ACTIVITY_COMPOSE}"
    const val ANDROIDX_COMPOSE_PREVIEW = "androidx.compose.ui:ui-tooling-preview"
    const val ANDROIDX_COMPOSE_UI_TOOLING = "androidx.compose.ui:ui-tooling"
    const val ANDROIDX_COMPOSE_LIFECYCLE =
        "androidx.lifecycle:lifecycle-runtime-compose:${Version.Androidx.LIFECYCLE}"
    const val COMPOSE_UI = "androidx.compose.ui:ui"
    const val COMPOSE_MATERIAL = "androidx.compose.material:material"
    const val COMPOSE_MATERIAL3 = "androidx.compose.material3:material3"
    const val COMPOSE_MATERIAL3_WINDOW_SIZE =
        "androidx.compose.material3:material3-window-size-class"
    const val COMPOSE_NAVIGATION =
        "androidx.navigation:navigation-compose:${Version.Compose.NAVIGATION}"
    const val COMPOSE_VIEW_MODEL = "androidx.lifecycle:lifecycle-viewmodel-compose"
    const val COMPOSE_HILT_NAVIGATION =
        "androidx.hilt:hilt-navigation-compose:${Version.Compose.HILT_NAVIGATION}"
    const val COMPOSE_UI_TEST = "androidx.compose.ui:ui-test-junit4"
    const val COMPOSE_UI_TEST_MANIFEST = "androidx.compose.ui:ui-test-manifest"
    const val COMPOSE_CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout-compose:${Version.Compose.CONSTRAINTLAYOUT}"


    const val HILT_ANDROID = "com.google.dagger:hilt-android:${Version.HILT}"
    const val HILT_COMPILER = "com.google.dagger:hilt-compiler:${Version.HILT}"
    const val HILT_TESTING = "com.google.dagger:hilt-android-testing:${Version.HILT}"
    const val HILT_ANDROID_COMPILER = "com.google.dagger:hilt-android-compiler:${Version.HILT}"

    const val ANDROIDX_JUNIT = "androidx.test.ext:junit:${Version.Androidx.ANDROIDX_JUNIT}"
    const val ANDROIDX_TEST_CORE = "androidx.test:core:${Version.Androidx.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_RUNNER = "androidx.test:runner:${Version.Androidx.ANDROIDX_TEST}"

    const val JUNIT = "junit:junit:${Version.Junit.JUNIT}"
    const val JUNIT5_API = "org.junit.jupiter:junit-jupiter-api:${Version.Junit.JUNIT5}"
    const val JUNIT5_ENGINE = "org.junit.jupiter:junit-jupiter-engine:${Version.Junit.JUNIT5}"
    const val JUNIT5_PARAMS = "org.junit.jupiter:junit-jupiter-params:${Version.Junit.JUNIT5}"
    const val JUNIT5_VINTAGE = "org.junit.vintage:junit-vintage-engine:${Version.Junit.VINTAGE}"
    const val JUNIT_KTX = "androidx.test.ext:junit-ktx:${Version.Junit.KTX}"

    const val MOCKITO_CORE = "org.mockito:mockito-core:${Version.Mockito.CORE}"
    const val MOCKITO_INLINE = "org.mockito:mockito-inline:${Version.Mockito.CORE}"
    const val MOCKITO_JUNIT = "org.mockito:mockito-junit-jupiter:${Version.Mockito.CORE}"
    const val MOCKITO_KOTLIN = "org.mockito.kotlin:mockito-kotlin:${Version.Mockito.KOTLIN}"

    const val ANDROIDX_ESPRESSO_CORE =
        "androidx.test.espresso:espresso-core:${Version.ESPRESSO_CORE}"

    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Version.Retrofit.RETROFIT}"
    const val RETROFIT_GSON_CONVERTER =
        "com.squareup.retrofit2:converter-gson:${Version.Retrofit.RETROFIT}"
    const val GSON = "com.google.code.gson:gson:${Version.Retrofit.GSON}"
    const val RETROFIT_COROUTINES_ADAPTER =
        "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Version.Retrofit.COROUTINES_ADAPTER}"
    const val OKHTTP_LOGGING =
        "com.squareup.okhttp3:logging-interceptor:${Version.Retrofit.OKHTTP_LOGGING}"

    const val COROUTINES_CORE =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.COROUTINES}"
    const val COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.COROUTINES}"
    const val COROUTINES_TEST =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.COROUTINES}"

    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Version.ROOM_VERSION}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Version.ROOM_VERSION}"
    const val ROOM_TEST = "androidx.room:room-testing:${Version.ROOM_VERSION}"
    const val ROOM_COROUTINE = "androidx.room:room-coroutines:${Version.ROOM_VERSION}"
    const val ROOM_COMMON = "androidx.room:room-common:${Version.ROOM_VERSION}"
    const val ROOM_KTX = "androidx.room:room-ktx:${Version.ROOM_VERSION}"

    const val COIL_COMPOSE = "io.coil-kt:coil-compose:${Version.COIL}"
    const val ACCOMPANIST_PLACEHOLDER =
        "com.google.accompanist:accompanist-placeholder-material:${Version.ACCOMPANIST}"
}

fun DependencyHandler.androidxCore() = implementation(Dependencies.ANDROIDX_CORE_KTX)
fun DependencyHandler.coilCompose() {
    implementation(Dependencies.COIL_COMPOSE)
}

fun DependencyHandler.compose() {
    implementation(platform(Dependencies.COMPOSE_BOM))
    implementation(Dependencies.COMPOSE_HILT_NAVIGATION)
    implementation(Dependencies.ANDROID_ACTIVITY_COMPOSE)
    implementation(Dependencies.ANDROIDX_COMPOSE_PREVIEW)
    implementation(Dependencies.ANDROIDX_COMPOSE_UI_TOOLING)
    implementation(Dependencies.ANDROIDX_COMPOSE_LIFECYCLE)
    implementation(Dependencies.COMPOSE_UI)
}

fun DependencyHandler.placeholder() = implementation(Dependencies.ACCOMPANIST_PLACEHOLDER)

fun DependencyHandler.room() {
    implementation(ROOM_RUNTIME)
    annotationProcessor(ROOM_COMPILER)
    kapt(ROOM_COMPILER)
    testImplementation(ROOM_TEST)
    implementation(ROOM_KTX)
}

fun DependencyHandler.composeMaterial() {
    implementation(Dependencies.COMPOSE_MATERIAL)
    implementation(Dependencies.COMPOSE_MATERIAL3)
    implementation(Dependencies.COMPOSE_MATERIAL3_WINDOW_SIZE)
}

fun DependencyHandler.composeTest() {
    androidTestImplementation(platform(Dependencies.COMPOSE_BOM))
    androidTestImplementation(Dependencies.COMPOSE_UI_TEST)
    implementation(Dependencies.COMPOSE_UI_TEST)
    debugImplementation(Dependencies.COMPOSE_UI_TEST_MANIFEST)
}

fun DependencyHandler.composeNavigation() {
    implementation(Dependencies.COMPOSE_NAVIGATION)
}

fun DependencyHandler.composeViewModel() {
    implementation(Dependencies.COMPOSE_VIEW_MODEL)
}

fun DependencyHandler.composeConstraintLayout() {
    implementation(Dependencies.COMPOSE_CONSTRAINT_LAYOUT)
}

fun DependencyHandler.coroutines() {
    implementation(Dependencies.COROUTINES_CORE)
    implementation(Dependencies.COROUTINES_ANDROID)
    implementation(Dependencies.COROUTINES_TEST)
}

fun DependencyHandler.retrofit() {
    implementation(Dependencies.RETROFIT)
    implementation(Dependencies.RETROFIT_GSON_CONVERTER)
    implementation(Dependencies.GSON)
    implementation(Dependencies.OKHTTP_LOGGING)
    implementation(Dependencies.RETROFIT_COROUTINES_ADAPTER)
}

fun DependencyHandler.gson() = implementation(Dependencies.GSON)

fun DependencyHandler.hilt() {
    implementation(Dependencies.HILT_ANDROID)
    kapt(Dependencies.HILT_COMPILER)
}

fun DependencyHandler.hiltTest() {
    testImplementation(Dependencies.HILT_TESTING)
    implementation(Dependencies.HILT_TESTING)
    kaptTest(Dependencies.HILT_ANDROID_COMPILER)
    androidTestImplementation(Dependencies.HILT_TESTING)
    kaptAndroidTest(Dependencies.HILT_ANDROID_COMPILER)
}

fun DependencyHandler.androidXTest() {
    androidTestImplementation(Dependencies.ANDROIDX_JUNIT)
    androidTestImplementation(Dependencies.ANDROIDX_TEST_CORE)
    androidTestImplementation(Dependencies.ANDROIDX_TEST_RUNNER)
    implementation(Dependencies.ANDROIDX_TEST_RUNNER)
}

fun DependencyHandler.junit4() {
    implementation(JUNIT_KTX)
    testImplementation(Dependencies.JUNIT)
}

fun DependencyHandler.junit5() {
    testImplementation(Dependencies.JUNIT5_API)
    implementation(Dependencies.JUNIT5_API)
    testImplementation(Dependencies.JUNIT5_PARAMS)
    testImplementation(Dependencies.JUNIT5_VINTAGE)
    testRuntimeOnly(Dependencies.JUNIT5_ENGINE)
}

fun DependencyHandler.mockito() {
    testImplementation(Dependencies.MOCKITO_CORE)
    implementation(Dependencies.MOCKITO_CORE)
    testImplementation(Dependencies.MOCKITO_JUNIT)
    testImplementation(Dependencies.MOCKITO_INLINE)
    testImplementation(Dependencies.MOCKITO_KOTLIN)
}

fun DependencyHandler.moduleDependency(path: String) {
    implementation(project(path))
}

fun DependencyHandler.espresso() = androidTestImplementation(Dependencies.ANDROIDX_ESPRESSO_CORE)

fun DependencyHandler.lifecycle() = implementation(Dependencies.ANDROID_LIFECYCLE_RUNTIME)

private fun DependencyHandler.kapt(depName: String) = add("kapt", depName)
private fun DependencyHandler.kaptTest(depName: String) = add("kaptTest", depName)
private fun DependencyHandler.kaptAndroidTest(depName: String) = add("kaptAndroidTest", depName)

fun DependencyHandler.implementation(depName: String) = add("implementation", depName)
fun DependencyHandler.implementation(dependency: Dependency) = add("implementation", dependency)

fun DependencyHandler.androidTestImplementation(depName: String) =
    add("androidTestImplementation", depName)

fun DependencyHandler.androidTestImplementation(dependency: Dependency) =
    add("androidTestImplementation", dependency)

fun DependencyHandler.testImplementation(depName: String) = add("testImplementation", depName)
fun DependencyHandler.testRuntimeOnly(depName: String) = add("testRuntimeOnly", depName)

fun DependencyHandler.debugImplementation(depName: String) = add("debugImplementation", depName)

private fun DependencyHandler.compileOnly(depName: String) = add("compileOnly", depName)
private fun DependencyHandler.runtimeOnly(depName: String) = add("runtimeOnly", depName)

private fun DependencyHandler.api(depName: String) = add("api", depName)

private fun DependencyHandler.annotationProcessor(depName: String) =
    add("annotationProcessor", depName)
