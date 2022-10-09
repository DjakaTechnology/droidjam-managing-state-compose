package id.djaka.droidjam.shared.core.ios

import id.djaka.droidjam.common.CoreApp
import id.djaka.droidjam.shared.core.ios.util.SuspendedWrapper
import id.djaka.droidjam.shared.locale.presentation.api.di.LocalePresentationApiDIManager
import kotlinx.coroutines.MainScope

@ThreadLocal
object SharedModule {
    private var isInitialized = false
    val scope = MainScope()

    fun init() = SuspendedWrapper {
        if (!isInitialized) {
            CoreApp.init()
            isInitialized = true
        }
    }

    object Locale {
        fun getAPIDIManager() = LocalePresentationApiDIManager

        fun getPresenterProvider() = getAPIDIManager().presenterProvider
    }
}