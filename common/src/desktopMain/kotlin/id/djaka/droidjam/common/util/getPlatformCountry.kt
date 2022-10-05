package id.djaka.droidjam.common.util

import java.util.Locale

actual fun getPlatformCountry(): String {
    return Locale.getDefault().country
}