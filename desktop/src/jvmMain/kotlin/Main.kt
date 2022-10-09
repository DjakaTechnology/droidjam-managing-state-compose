// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import id.djaka.droidjam.common.App
import id.djaka.droidjam.shared.core.di.CoreDIManager
import id.djaka.droidjam.shared.core_ui.theme.CoreTheme
import id.djaka.droidjam.shared.locale.app.di.LocaleDIManager
import id.djaka.droidjam.shared.locale.ui.country_picker.CountryPickerScreen


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        var isInitialized by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            CoreDIManager.init()
            LocaleDIManager.init()

            isInitialized = true
        }

        if (isInitialized) {
            App {
                CountryPickerScreen()
            }
        }
    }
}
