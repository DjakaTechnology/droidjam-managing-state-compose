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
    implementation(project(":shared:core"))
    implementation(project(":shared:core-ui"))
    implementation(project(":shared:locale:locale-ui"))
    implementation(project(":shared:locale:locale-presentation-api"))
    implementation(project(":shared:locale:locale-app")) // Need to reference this because rxJava only exist in android
    implementation(Libraries.composeActivity)
    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
    implementation(compose.material3)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(Libraries.rxJava)
    implementation(Libraries.coroutineRx)
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
            signingConfig = signingConfigs.getByName("release")
        }
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
    }

}