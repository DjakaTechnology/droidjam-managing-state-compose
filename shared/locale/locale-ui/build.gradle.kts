import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id(BuildPlugins.serialization)
    id(BuildPlugins.kotlinParcelize)
    id(BuildPlugins.molecule)
}

kotlin {
    android()

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = DroidJam.desktopKotlinJvmTarget
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:core"))
                implementation(project(":shared:core-ui"))
                implementation(compose.runtime)
                implementation(compose.foundation)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
                implementation(Libraries.serialization)
                implementation(Libraries.settings)
                implementation(Libraries.settingsNoArgs)
                implementation(Libraries.settingsCoroutines)
                implementation(Libraries.sqlDelightCoroutines)
                implementation(Libraries.kamelImage)
                implementation(Libraries.ktorClientCore)
                implementation(Libraries.coroutinesCore)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Libraries.sqlDelightAndroidDriver)
                implementation(Libraries.ktorClientCIO)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
            }
        }
        val androidTest by getting
        val desktopMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation("dev.icerock.moko:resources-compose:0.20.1")
                implementation(Libraries.sqlDelightJvmDriver)
                implementation(Libraries.ktorClientCIO)
            }
        }
    }
}

android {
    namespace = "id.djaka.droidjam.locale"
    compileSdk = DroidJam.compileSdk
    defaultConfig {
        minSdk = DroidJam.minSdk
        targetSdk = DroidJam.targetSdk
    }
}