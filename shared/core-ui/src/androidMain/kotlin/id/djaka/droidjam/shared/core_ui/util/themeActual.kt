package id.djaka.droidjam.shared.core_ui.util

import android.os.Build

actual fun isSupportDynamicColor(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}