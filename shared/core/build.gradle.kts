import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id(BuildPlugins.jetbrainCompose)
    id(BuildPlugins.kotlinParcelize)
    id(BuildPlugins.sqlDelight)
}

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = DroidJam.desktopKotlinJvmTarget
        }
    }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "core"
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(Libraries.coroutinesCore)
                implementation(Libraries.sqlDelightCoroutines)
                implementation(Libraries.settings)
                implementation(Libraries.settingsCoroutines)
                implementation(Libraries.settingsNoArgs)
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
            }
        }
        val androidTest by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation(Libraries.sqlDelightNativeDriver)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

        val desktopMain by getting {
            dependencies {
                implementation(Libraries.sqlDelightJvmDriver)
            }
        }
    }
}

android {
    namespace = "id.djaka.droidjam.shared.core"
    compileSdk = DroidJam.compileSdk
    defaultConfig {
        minSdk = DroidJam.minSdk
        targetSdk = DroidJam.targetSdk
    }
}

sqldelight {
    database("DroidJamDB") {
        packageName = "id.djaka.droidjam.database"
        deriveSchemaFromMigrations = true
        verifyMigrations = true
    }
}
