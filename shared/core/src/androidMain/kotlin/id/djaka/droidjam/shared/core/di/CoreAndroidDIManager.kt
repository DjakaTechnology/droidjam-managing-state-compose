package id.djaka.droidjam.shared.core.di

import android.app.Application

object CoreAndroidDIManager {
    lateinit var appComponent: AndroidComponent
}

class AndroidComponent(
    val app: Application
)