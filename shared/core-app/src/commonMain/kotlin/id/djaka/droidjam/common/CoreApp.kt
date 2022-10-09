package id.djaka.droidjam.common

import id.djaka.droidjam.shared.booking.app.di.BookingDIManager
import id.djaka.droidjam.shared.core.di.CoreDIManager
import id.djaka.droidjam.shared.locale.app.di.LocaleDIManager
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object CoreApp {
    private var isInitialized = false

    suspend fun init() {
        if (!isInitialized) {
            CoreDIManager.init()
            LocaleDIManager.init()
            BookingDIManager.init()
        }

        isInitialized = true
    }
}