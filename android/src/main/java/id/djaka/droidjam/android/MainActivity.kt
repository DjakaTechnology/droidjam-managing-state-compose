package id.djaka.droidjam.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import id.djaka.droidjam.android.ui.country_picker.CountryPickerScreenRx
import id.djaka.droidjam.shared.core_ui.App
import id.djaka.droidjam.shared.locale.ui.country_picker.CountryPickerScreen
import id.djaka.droidjam.shared.locale.ui.country_picker.CountryPickerScreenFlow
import id.djaka.droidjam.shared.locale.ui.country_picker.CountryPickerScreenFlowLikeRx

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App {
//                CountryPickerScreen()
//                CountryPickerScreenFlow()
//                CountryPickerScreenRx()
                CountryPickerScreenFlowLikeRx()
            }
        }
    }
}