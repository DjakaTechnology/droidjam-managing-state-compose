package id.djaka.droidjam.shared.core_ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import id.djaka.droidjam.shared.core_ui.theme.CoreTheme
import id.djaka.droidjam.shared.core_ui.util.provideKamelComposition

@Composable
fun App(content: @Composable () -> Unit) {
    CompositionLocalProvider(provideKamelComposition()) {
        CoreTheme {
            content()
        }
    }
}
