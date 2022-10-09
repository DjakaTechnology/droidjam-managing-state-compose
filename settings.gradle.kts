// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("multiplatform").version(extra["kotlin.version"] as String)
        kotlin("android").version(extra["kotlin.version"] as String)
        kotlin("plugin.serialization").version(extra["kotlin.version"] as String)
        id("com.android.application").version(extra["agp.version"] as String)
        id("com.android.library").version(extra["agp.version"] as String)
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
    }
}

rootProject.name = "droidjam"

include(":android", ":desktop", ":common")
include(":shared:core")
include(":shared:locale:locale-ui")
include(":shared:core-ui")
include(":shared:locale:locale-app")
include(":shared:core-molecule")
include(":shared:core-ios")
include(":shared:locale:locale-presentation-api")
include(":js")
