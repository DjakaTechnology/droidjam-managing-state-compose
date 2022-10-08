import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "id.djaka"
version = "1.0-SNAPSHOT"


kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = DroidJam.desktopKotlinJvmTarget
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(project(":shared:core"))
                implementation(project(":shared:core-ui"))
                implementation(project(":shared:locale:locale-ui"))
                implementation(project(":shared:locale:locale-presentation-api"))
                implementation(project(":shared:locale:locale-app"))
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "droidjam"
            packageVersion = "1.0.0"
        }
    }
}
