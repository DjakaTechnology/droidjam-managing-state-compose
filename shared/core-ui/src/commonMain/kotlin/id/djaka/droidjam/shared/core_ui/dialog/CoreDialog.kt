package id.djaka.droidjam.shared.core_ui.dialog

import androidx.compose.runtime.Composable

@Composable
expect fun CoreDialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit)