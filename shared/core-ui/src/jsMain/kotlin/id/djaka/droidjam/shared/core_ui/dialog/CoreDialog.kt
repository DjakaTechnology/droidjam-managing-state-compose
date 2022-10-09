package id.djaka.droidjam.shared.core_ui.dialog

import androidx.compose.runtime.Composable

@Composable
actual fun CoreDialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    //TODO: Find a way to show dialog
    content()
}