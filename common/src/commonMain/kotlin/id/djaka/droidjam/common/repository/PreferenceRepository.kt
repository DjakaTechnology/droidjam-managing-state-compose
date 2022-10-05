@file:OptIn(ExperimentalSettingsApi::class)

package id.djaka.droidjam.common.repository

import com.russhwolf.settings.*
import com.russhwolf.settings.coroutines.getBooleanFlow
import com.russhwolf.settings.coroutines.getStringFlow
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import id.djaka.droidjam.common.util.Constant
import java.util.prefs.Preferences

class PreferenceRepository {
    private val setting = Settings() as ObservableSettings

    var colorMode by setting.string(COLOR_MODE, Constant.ThemeMode.AUTO.value)
    val getColorModeFlow get() = setting.getStringFlow(COLOR_MODE, Constant.ThemeMode.AUTO.value)

    var isUseDynamicColor by setting.boolean(IS_USE_DYNAMIC_COLOR, true)
    val getDynamicColorFlow = setting.getBooleanFlow(IS_USE_DYNAMIC_COLOR, true)

    var defaultCountry by setting.nullableString(DEFAULT_COUNTRY)
    val getDefaultCountryFlow = setting.getStringOrNullFlow(DEFAULT_COUNTRY)

    companion object {
        const val COLOR_MODE = "pref.colorMode"
        const val IS_USE_DYNAMIC_COLOR = "pref.isUseDynamicColor"
        const val DEFAULT_COUNTRY = "pref.defaultCountry"
    }
}