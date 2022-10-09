plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id(BuildPlugins.mokoSwift)
}

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "Shared"
            isStatic = false
            transitiveExport = true
            linkerOpts("-lsqlite3")
            export(project(":shared:locale:locale-presentation-api"))
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:core"))
                implementation(project(":shared:core-app"))
                api(project(":shared:locale:locale-presentation-api"))

                implementation(Libraries.coroutinesCore)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
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
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

kswift {
    install(dev.icerock.moko.kswift.plugin.feature.SealedToSwiftEnumFeature) {
        filter = excludeFilter(
            "ClassContext/org.jetbrains.compose.runtime:runtime/androidx/compose/runtime/snapshots/Snapshot",
            "ClassContext/com.squareup.sqldelight:native-driver/com/squareup/sqldelight/drivers/native/ConnectionWrapper",
            "ClassContext/org.jetbrains.kotlinx:kotlinx-datetime/kotlinx/datetime/DateTimePeriod",
            "ClassContext/org.jetbrains.kotlinx:kotlinx-serialization-core/kotlinx/serialization/descriptors/PolymorphicKind",
            "ClassContext/org.jetbrains.kotlinx:kotlinx-serialization-json/kotlinx/serialization/json/Json"
        )
    }
}