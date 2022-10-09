import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("com.android.library")
//    id("dev.icerock.mobile.multiplatform-resources")
    id(BuildPlugins.serialization)
    id(BuildPlugins.sqlDelight)
    id(BuildPlugins.molecule)
    id(BuildPlugins.jetbrainCompose)
    id(BuildPlugins.kotlinParcelize)
}

group = "id.djaka"
version = "1.0-SNAPSHOT"

kotlin {
    android()
    jvm("jvm") {
        compilations.all {
            kotlinOptions.jvmTarget = DroidJam.jvmTarget
        }
    }
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:core"))
                implementation(project(":shared:core-ui"))
                implementation(project(":shared:core-molecule"))
                implementation(project(":shared:locale:locale-app"))
                implementation(project(":shared:locale:locale-presentation-api"))
                implementation(compose.runtime)
                implementation(compose.foundation)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
                implementation(Libraries.serialization)
//                implementation("dev.icerock.moko:resources:0.20.1")
                implementation(Libraries.settings)
                implementation(Libraries.settingsNoArgs)
                implementation(Libraries.settingsCoroutines)
                implementation(Libraries.sqlDelightCoroutines)
                implementation(Libraries.kamelImage)
                implementation(Libraries.ktorClientCore)
                implementation(Libraries.coroutinesCore)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("dev.icerock.moko:resources-compose:0.20.1")
                implementation(Libraries.sqlDelightAndroidDriver)
                implementation(Libraries.ktorClientCIO)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation("dev.icerock.moko:resources-compose:0.20.1")
                implementation(Libraries.sqlDelightJvmDriver)
                implementation(Libraries.ktorClientCIO)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(TestLibraries.turbine)
                implementation(TestLibraries.coroutines)
                implementation(TestLibraries.mockk)
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
    signingConfigs {
        create("release") {
            storeFile = file("..\\droidjam.jks")
            storePassword = "droidjam"
            keyAlias = "droidjam"
            keyPassword = "droidjam"
        }
    }
    compileSdk = (DroidJam.compileSdk)
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res", "src/commonMain/resources")
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
    }

    defaultConfig {
        minSdk = DroidJam.minSdk
        targetSdk = DroidJam.targetSdk
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

//multiplatformResources {
//    multiplatformResourcesPackage = "id.djaka.droidjam" // required
////    multiplatformResourcesVisibility = MRVisibility.Internal // optional, default Public
//    iosBaseLocalizationRegion = "en" // optional, default "en"
//}