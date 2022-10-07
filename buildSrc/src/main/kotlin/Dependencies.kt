object DroidJam {
    const val minSdk = 21
    const val targetSdk = 33
    const val compileSdk = 33
    const val applicationId = "id.djaka.droidjam"

    // Version
    const val versionName = "1.0-SNAPSHOT"
    const val versionCode = 32
}

object Versions {
    // Kotlin
    const val kotlin = "1.7.10"

    // Compose
    const val compose = "1.2.0"
    const val composeMaterial3 = "1.0.0-beta02"

    // Coroutines
    const val coroutines = "1.6.4"

    // Serialization
    const val serialization = "1.4.0"

    const val sqlDelight = "1.5.4"

    const val settings = "1.0.0-alpha01"
    const val kamelImage = "0.4.1"
    const val ktor = "2.1.1"

    const val molecule = "0.5.0-beta01"
    const val turbine = "0.11.0"
    const val mockk = "1.13.2"
}

object Libraries {
    const val material3 = "com.google.android.material:material:1.6.1"

    // Compose
    const val composeCompiler = "androidx.compose.compiler:compiler:${Versions.compose}"
    const val composeUI = "androidx.compose.ui:ui:${Versions.compose}"
    const val composePreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
    const val composeGoogleFonts = "androidx.compose.ui:ui-text-google-fonts:${Versions.compose}"

    // Compose Material 3
    const val composeMaterial3 = "androidx.compose.material3:material3:${Versions.composeMaterial3}"
    const val composeMaterial3WindowSize = "androidx.compose.material3:material3-window-size-class:${Versions.composeMaterial3}"

    // Compose Activtiy
    const val composeActivity = "androidx.activity:activity-compose:1.5.1"

    // Coroutines
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutineRx = "org.jetbrains.kotlinx:kotlinx-coroutines-rx3:${Versions.coroutines}"

    // Serialization
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}"

    // SQL Delight
    const val sqlDelightNativeDriver = "com.squareup.sqldelight:native-driver:${Versions.sqlDelight}"
    const val sqlDelightJvmDriver = "com.squareup.sqldelight:sqlite-driver:${Versions.sqlDelight}"
    const val sqlDelightAndroidDriver = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"
    const val sqlDelightCoroutines = "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"

    // Settings
    const val settings = "com.russhwolf:multiplatform-settings:${Versions.settings}"
    const val settingsNoArgs = "com.russhwolf:multiplatform-settings-no-arg:${Versions.settings}"
    const val settingsCoroutines= "com.russhwolf:multiplatform-settings-coroutines:${Versions.settings}"

    // Image Loader
    const val kamelImage = "com.alialbaali.kamel:kamel-image:${Versions.kamelImage}"

    // Ktor
    const val ktorClientCore = "io.ktor:ktor-client-core:${Versions.ktor}"
    const val ktorClientCIO = "io.ktor:ktor-client-cio:${Versions.ktor}"

    // RxJava
    const val rxJava = "io.reactivex.rxjava3:rxjava:3.1.5"
}

object TestLibraries {
    val turbine = "app.cash.turbine:turbine:${Versions.turbine}"
    val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    val mockk = "io.mockk:mockk:${Versions.mockk}"
}

object BuildClassPath {
    const val kotlinSerializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
    const val sqlDelightPlugin = "com.squareup.sqldelight:gradle-plugin:1.5.3"
    const val moleculePlugin = "app.cash.molecule:molecule-gradle-plugin:${Versions.molecule}"
}

object BuildPlugins {
    const val sqlDelight = "com.squareup.sqldelight"
    const val molecule = "app.cash.molecule"
}