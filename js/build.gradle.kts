import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id(BuildPlugins.sqlDelight)
    id(BuildPlugins.jetbrainCompose)
}

group = "id.djaka"
version = "1.0-SNAPSHOT"


kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:core-app"))
                implementation(project(":shared:core"))
                implementation(project(":shared:core-ui"))
                implementation(project(":shared:locale:locale-presentation-api"))
                implementation(project(":shared:locale:locale-ui"))
                implementation(project(":shared:booking:booking-presentation-api"))
                implementation(project(":shared:booking:booking-ui"))
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.runtime)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(Libraries.sqlDelightJSDriver)
                implementation(Libraries.kamelImageJs)
                implementation(npm("sql.js", "1.7.0"))

                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
                implementation(devNpm("sass-loader", "^13.0.0"))
                implementation(devNpm("sass", "^1.52.1"))
            }
        }
    }
}

compose.experimental {
    web.application {}
}

// a temporary workaround for a bug in jsRun invocation - see https://youtrack.jetbrains.com/issue/KT-48273
afterEvaluate {
    rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
        versions.webpackDevServer.version = "4.0.0"
        versions.webpackCli.version = "4.10.0"
        nodeVersion = "16.0.0"
    }
}

// TODO: remove when https://youtrack.jetbrains.com/issue/KT-50778 fixed
project.tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile::class.java).configureEach {
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xir-dce-runtime-diagnostic=log"
    )
}