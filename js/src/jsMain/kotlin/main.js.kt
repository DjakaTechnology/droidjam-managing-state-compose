import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import id.djaka.droidjam.common.CoreApp
import id.djaka.droidjam.shared.core.di.CoreDIManager
import id.djaka.droidjam.shared.core_ui.App
import id.djaka.droidjam.shared.locale.app.di.LocaleDIManager
import id.djaka.droidjam.shared.locale.ui.country_picker.CountryPickerScreen
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        Window("Chat") {
            var isInitialized by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                CoreApp.init()

                isInitialized = true
            }

            if (isInitialized) {
                App {
                    CountryPickerScreen()
                }
            }
        }
    }
}