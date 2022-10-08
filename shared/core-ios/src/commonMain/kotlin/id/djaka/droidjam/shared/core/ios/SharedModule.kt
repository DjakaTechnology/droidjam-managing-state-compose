package id.djaka.droidjam.shared.core.ios

import id.djaka.droidjam.shared.locale.app.di.LocaleDIManager
import id.djaka.droidjam.shared.locale.presentation.api.di.LocalePresentationApiDIManager

@ThreadLocal
object SharedModule {
    private var isInitialized = false

    fun init() {
        if (!isInitialized) {
            LocaleDIManager.init()
            isInitialized = true
        }
    }

    object Locale {
        fun getAPIDIManager() = LocalePresentationApiDIManager

        fun getPresenterProvider() = getAPIDIManager().presenterProvider
    }
}