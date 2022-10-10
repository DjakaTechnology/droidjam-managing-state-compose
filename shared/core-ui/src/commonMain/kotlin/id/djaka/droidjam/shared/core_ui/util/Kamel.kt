package id.djaka.droidjam.shared.core_ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.Modifier

expect fun provideKamelComposition(): ProvidedValue<*>

@Composable
expect fun ImageLoader(
    modifier: Modifier,
    string: String,
    onLoading: @Composable (Float) -> Unit,
    onFailure: @Composable (Throwable) -> Unit,
)