plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

group = "id.djaka"
version = "1.0-SNAPSHOT"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

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
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:core"))
                implementation(project(":shared:locale:locale-app"))
                implementation(project(":shared:booking:booking-app"))
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
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