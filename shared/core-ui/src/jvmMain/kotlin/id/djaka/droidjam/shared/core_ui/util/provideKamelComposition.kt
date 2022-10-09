package id.djaka.droidjam.shared.core_ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.Modifier
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.KamelImage
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.config.svgDecoder
import io.kamel.image.lazyPainterResource
import io.ktor.http.Url

actual fun provideKamelComposition(): ProvidedValue<*> {
    return LocalKamelConfig provides KamelConfig {
        takeFrom(KamelConfig.Default)
        resourcesFetcher()
        svgDecoder()
    }
}

@Composable
actual fun ImageLoader(modifier: Modifier, string: String, onLoading: @Composable (Float) -> Unit, onFailure: @Composable (Throwable) -> Unit) {
    KamelImage(
        resource = lazyPainterResource(data = Url(string)),
        contentDescription = "flag",
        modifier = modifier,
        onLoading = {
            onLoading(it)
        },
        onFailure = {
            onFailure(it)
        }
    )
}