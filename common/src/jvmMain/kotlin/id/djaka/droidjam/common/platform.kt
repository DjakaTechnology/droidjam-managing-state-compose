package id.djaka.droidjam.common

actual fun getPlatformName(): String {
    return "Desktop"
}

actual fun getPlatform(): Platform {
    return Platform.DESKTOP
}