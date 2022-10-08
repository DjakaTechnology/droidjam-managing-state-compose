import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id(BuildPlugins.molecule)
    id("com.android.library")
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
                implementation(compose.runtime)
                implementation(compose.foundation)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
                implementation(Libraries.serialization)
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
            }
        }
    }
}

android {
    namespace = "id.djaka.droidjam.shared.core_ui"
    compileSdk = DroidJam.compileSdk
    defaultConfig {
        minSdk = DroidJam.minSdk
        targetSdk = DroidJam.targetSdk
    }
}