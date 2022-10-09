import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group "id.djaka"
version "1.0-SNAPSHOT"

buildscript {
    dependencies {
//        classpath("dev.icerock.moko:resources-generator:0.20.1")
        classpath(BuildClassPath.kotlinSerializationPlugin)
        classpath(BuildClassPath.sqlDelightPlugin)
        classpath(BuildClassPath.moleculePlugin)
        classpath(BuildClassPath.mokoSwift)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=report"
            )
        }
    }
}

plugins {
    kotlin("multiplatform") apply false
    kotlin("android") apply false
    kotlin("plugin.serialization") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
}