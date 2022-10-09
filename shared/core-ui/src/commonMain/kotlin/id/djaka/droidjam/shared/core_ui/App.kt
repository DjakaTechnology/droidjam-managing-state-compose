package id.djaka.droidjam.shared.core_ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import id.djaka.droidjam.shared.core_ui.theme.CoreTheme
import id.djaka.droidjam.shared.core_ui.util.provideKamelConfig
import io.kamel.image.config.LocalKamelConfig

@Composable
fun App(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalKamelConfig provides provideKamelConfig()) {
        CoreTheme {
            content()
        }
    }
}
