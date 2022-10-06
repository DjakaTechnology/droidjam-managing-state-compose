package id.djaka.droidjam.common.ui.dialog

import androidx.compose.runtime.Composable

@Composable
expect fun CoreDialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit)