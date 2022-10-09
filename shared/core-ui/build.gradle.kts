import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("com.android.library")
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
                implementation(compose.runtime)
                implementation(compose.foundation)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
                implementation(Libraries.serialization)
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
                implementation(Libraries.kamelImage)
            }
        }
        val androidTest by getting

        val jvmMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation(Libraries.kamelImage)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(Libraries.sqlDelightJSDriver)
            }
        }
    }
}

compose.experimental {
    web.application {}
}

android {
    namespace = "id.djaka.droidjam.shared.core_ui"
    compileSdk = DroidJam.compileSdk
    defaultConfig {
        minSdk = DroidJam.minSdk
        targetSdk = DroidJam.targetSdk
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}