package id.djaka.droidjam.common.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
actual fun CoreDialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    Dialog(onDismissRequest) {
        content()
    }
}