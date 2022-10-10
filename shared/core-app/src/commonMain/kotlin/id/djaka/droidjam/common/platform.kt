package id.djaka.droidjam.common

expect fun getPlatformName(): String

expect fun getPlatform(): Platform

enum class Platform {
    ANDROID,
    DESKTOP,
    IOS,
    JS
}