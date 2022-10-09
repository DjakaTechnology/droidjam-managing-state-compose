package id.djaka.droidjam.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import id.djaka.droidjam.shared.core_ui.App
import id.djaka.droidjam.shared.locale.ui.country_picker.CountryPickerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App {
                CountryPickerScreen()
            }
        }
    }
}