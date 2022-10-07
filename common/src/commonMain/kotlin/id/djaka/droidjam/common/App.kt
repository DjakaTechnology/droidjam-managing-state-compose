package id.djaka.droidjam.common

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import id.djaka.droidjam.shared.core_ui.util.provideKamelConfig
import id.djaka.droidjam.shared.locale.ui.country_picker.CountryPickerScreen
import io.kamel.image.config.LocalKamelConfig

@Composable
fun App() {
    CompositionLocalProvider(LocalKamelConfig provides provideKamelConfig()) {
        rememberCoroutineScope().launchMolecule(RecompositionClock.ContextClock, {})
        Surface {
            CountryPickerScreen()
//            BookingScreen()
        }
    }
}
