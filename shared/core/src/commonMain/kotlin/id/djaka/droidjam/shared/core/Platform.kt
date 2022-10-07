package id.djaka.droidjam.shared.core

interface Platform {
    val name: String
    val platformType: PlatformType
}

enum class PlatformType {
    ANDROID,
    IOS,
    DESKTOP
}

expect fun getPlatform(): Platform