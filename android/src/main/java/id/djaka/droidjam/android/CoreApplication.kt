package id.djaka.droidjam.android

import android.app.Application
import id.djaka.droidjam.shared.core.di.AndroidComponent
import id.djaka.droidjam.shared.core.di.CoreAndroidDIManager
import id.djaka.droidjam.shared.locale.app.di.LocaleDIManager

class CoreApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        LocaleDIManager.init()
        CoreAndroidDIManager.appComponent = AndroidComponent(this)
    }
}