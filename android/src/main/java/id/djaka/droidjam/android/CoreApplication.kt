package id.djaka.droidjam.android

import android.app.Application
import id.djaka.droidjam.shared.core.di.AndroidComponent
import id.djaka.droidjam.shared.core.di.CoreAndroidDIManager

class CoreApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        CoreAndroidDIManager.appComponent = AndroidComponent(this)
    }
}