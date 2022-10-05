package id.djaka.droidjam.common

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import id.djaka.droidjam.common.ui.booking.BookingScreen
import id.djaka.droidjam.common.ui.country_picker.CountryPickerScreen
import id.djaka.droidjam.common.util.provideKamelConfig
import io.kamel.image.config.LocalKamelConfig

@Composable
fun App() {
    CompositionLocalProvider(LocalKamelConfig provides provideKamelConfig()) {
        Surface {
//            CountryPickerScreen()
            BookingScreen()
        }
    }
}
