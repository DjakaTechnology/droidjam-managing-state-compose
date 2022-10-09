package id.djaka.droidjam.shared.core

actual fun getPlatform(): Platform {
    return JSPlatform()
}

class JSPlatform: Platform {
    override val name: String
        get() = "JS"
    override val platformType: PlatformType
        get() = PlatformType.JS

}