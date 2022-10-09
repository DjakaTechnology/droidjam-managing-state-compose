import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id(BuildPlugins.serialization)
    id(BuildPlugins.kotlinParcelize)
    id(BuildPlugins.molecule)
    id(BuildPlugins.jetbrainCompose)
}

kotlin {
    android()

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = DroidJam.jvmTarget
        }
    }

    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:core"))
                implementation(project(":shared:core-ui"))
                implementation(project(":shared:core-molecule"))
                implementation(project(":shared:locale:locale-presentation-api"))
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
        val jvmMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation("dev.icerock.moko:resources-compose:0.20.1")
                implementation(Libraries.sqlDelightJvmDriver)
                implementation(Libraries.ktorClientCIO)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(Libraries.sqlDelightJSDriver)
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

compose.experimental {
    web.application {}
}