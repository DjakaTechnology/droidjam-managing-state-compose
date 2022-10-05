package id.djaka.droidjam.common.util

import android.content.Context
import android.telephony.TelephonyManager
import id.djaka.droidjam.common.di.CoreAndroidDIManager
import java.util.*

actual fun getPlatformCountry(): String {
    val context = CoreAndroidDIManager.appComponent.app
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    return telephonyManager.simCountryIso.takeIf { it.isNotEmpty() }
        ?: telephonyManager.networkCountryIso.takeIf { it.isNotEmpty() }
        ?: Locale.getDefault().country
}