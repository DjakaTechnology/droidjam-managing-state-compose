import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id(BuildPlugins.serialization)
    id(BuildPlugins.kotlinParcelize)
    id(BuildPlugins.molecule)
    id(BuildPlugins.mokoSwift)
    id(BuildPlugins.jetbrainCompose)
}

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "SharedLocaleApp"
        }
    }

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
                implementation(project(":shared:core-molecule"))
                implementation(project(":shared:booking:booking-presentation-api"))
                implementation(compose.runtime)
                implementation(Libraries.serialization)
                implementation(Libraries.settings)
                implementation(Libraries.settingsNoArgs)
                implementation(Libraries.settingsCoroutines)
                implementation(Libraries.sqlDelightCoroutines)
                implementation(Libraries.coroutinesCore)
                implementation(Libraries.dateTime)
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
            }
        }
        val androidTest by getting
        val jvmMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation("dev.icerock.moko:resources-compose:0.20.1")
                implementation(Libraries.sqlDelightJvmDriver)
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
    namespace = "id.djaka.shared.booking_presentation_api"
    compileSdk = DroidJam.compileSdk
    defaultConfig {
        minSdk = DroidJam.minSdk
        targetSdk = DroidJam.targetSdk
    }
}

kswift {
    install(dev.icerock.moko.kswift.plugin.feature.SealedToSwiftEnumFeature)
}

compose.experimental {
    web.application {}
}