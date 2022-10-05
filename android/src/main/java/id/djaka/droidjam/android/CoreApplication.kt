package id.djaka.droidjam.android

import android.app.Application
import id.djaka.droidjam.common.di.AndroidComponent
import id.djaka.droidjam.common.di.CoreAndroidDIManager

class CoreApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        CoreAndroidDIManager.appComponent = AndroidComponent(this)
    }
}