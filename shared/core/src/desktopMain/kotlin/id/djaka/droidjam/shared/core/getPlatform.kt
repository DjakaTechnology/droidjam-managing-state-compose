package id.djaka.droidjam.shared.core

import id.djaka.droidjam.shared.core.Platform
import id.djaka.droidjam.shared.core.PlatformType

class DesktopPlatform : Platform {
    override val name: String = "Desktop"

    override val platformType: PlatformType = PlatformType.DESKTOP
}

actual fun getPlatform(): Platform {
    return DesktopPlatform()
}