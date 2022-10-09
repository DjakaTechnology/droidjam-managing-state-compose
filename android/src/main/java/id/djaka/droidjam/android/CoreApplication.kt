package id.djaka.droidjam.android

import android.app.Application
import id.djaka.droidjam.shared.core.di.AndroidComponent
import id.djaka.droidjam.shared.core.di.CoreAndroidDIManager
import id.djaka.droidjam.shared.core.di.CoreDIManager
import id.djaka.droidjam.shared.locale.app.di.LocaleDIManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CoreApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        MainScope().launch {
            CoreAndroidDIManager.appComponent = AndroidComponent(this@CoreApplication)
            CoreDIManager.init()
            LocaleDIManager.init()
        }
    }
}