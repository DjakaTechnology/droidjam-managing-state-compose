package id.djaka.droidjam.shared.core

import id.djaka.droidjam.shared.core.Platform
import id.djaka.droidjam.shared.core.PlatformType

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    override val platformType: PlatformType = PlatformType.ANDROID
}

actual fun getPlatform(): Platform = AndroidPlatform()