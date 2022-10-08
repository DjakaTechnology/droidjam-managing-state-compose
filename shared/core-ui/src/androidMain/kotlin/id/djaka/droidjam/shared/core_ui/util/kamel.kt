package id.djaka.droidjam.shared.core_ui.util

import id.djaka.droidjam.shared.core.di.CoreAndroidDIManager
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.imageBitmapDecoder
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.config.resourcesIdMapper

actual fun provideKamelConfig() = KamelConfig {
    val context = CoreAndroidDIManager.appComponent.app

    takeFrom(KamelConfig.Default)
    resourcesFetcher(context)
    resourcesIdMapper(context)
    imageBitmapDecoder()
}