package id.djaka.droidjam.common.di

import android.app.Application

object CoreAndroidDIManager {
    lateinit var appComponent: AndroidComponent
}

class AndroidComponent(
    val app: Application
)