import org.jetbrains.compose.compose

plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group "id.djaka.droidjam"
version "1.0-SNAPSHOT"

repositories {
    jcenter()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(project(":common"))
    implementation(Libraries.composeActivity)
    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
    implementation(compose.material3)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(Libraries.rxJava)
    implementation(Libraries.coroutineRx)
}

android {
    compileSdk = DroidJam.compileSdk
    defaultConfig {
        applicationId = DroidJam.applicationId
        versionCode = 1
        versionName = DroidJam.versionName

        minSdk = DroidJam.minSdk
        targetSdk = DroidJam.targetSdk
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
    }

}