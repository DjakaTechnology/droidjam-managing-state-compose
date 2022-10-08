package id.djaka.droidjam.shared.core

import id.djaka.droidjam.shared.core.Platform
import id.djaka.droidjam.shared.core.PlatformType
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    override val platformType: PlatformType = PlatformType.IOS
}

actual fun getPlatform(): Platform = IOSPlatform()