package id.djaka.droidjam.shared.core_ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import org.jetbrains.compose.web.dom.Text

actual fun provideKamelComposition(): ProvidedValue<*> {
    return staticCompositionLocalOf { } provides Unit
}

@Composable
actual fun ImageLoader(modifier: Modifier, string: String, onLoading: @Composable (Float) -> Unit, onFailure: @Composable (Throwable) -> Unit) {
    Text("Image Loader in JS is not supported yet")
}