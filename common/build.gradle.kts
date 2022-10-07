import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("dev.icerock.mobile.multiplatform-resources")
    id(BuildPlugins.sqlDelight)
    id(BuildPlugins.molecule)
}

group = "id.djaka"
version = "1.0-SNAPSHOT"

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
                implementation(Libraries.serialization)
                implementation("dev.icerock.moko:resources:0.20.1")
                implementation(Libraries.settings)
                implementation(Libraries.settingsNoArgs)
                implementation(Libraries.settingsCoroutines)
                implementation(Libraries.sqlDelightCoroutines)
                implementation(Libraries.kamelImage)
                implementation(Libraries.ktorClientCore)
                implementation(Libraries.coroutinesCore)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(TestLibraries.turbine)
                implementation(TestLibraries.coroutines)
                implementation(TestLibraries.mockk)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("androidx.appcompat:appcompat:1.2.0")
                implementation("androidx.core:core-ktx:1.3.1")
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
        val desktopMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation("dev.icerock.moko:resources-compose:0.20.1")
                implementation(Libraries.sqlDelightJvmDriver)
                implementation(Libraries.ktorClientCIO)
            }
        }
        val desktopTest by getting
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

multiplatformResources {
    multiplatformResourcesPackage = "id.djaka.droidjam" // required
//    multiplatformResourcesVisibility = MRVisibility.Internal // optional, default Public
    iosBaseLocalizationRegion = "en" // optional, default "en"
}

sqldelight {
    database("DroidJamDB") { // This will be the name of the generated database class.
        packageName = "id.djaka.droidjam.database"
        deriveSchemaFromMigrations = true
        verifyMigrations = true
    }
}
