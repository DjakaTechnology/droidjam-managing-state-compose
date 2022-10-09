package id.djaka.droidjam.shared.core_ui.util

import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.config.svgDecoder

actual fun provideKamelConfig() = KamelConfig {
    takeFrom(KamelConfig.Default)
    resourcesFetcher()
    svgDecoder()
}