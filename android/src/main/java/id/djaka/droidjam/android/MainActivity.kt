package id.djaka.droidjam.android

import id.djaka.droidjam.common.App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import id.djaka.droidjam.android.ui.country_picker.CountryPickerScreenRx
import id.djaka.droidjam.common.ui.country_picker.CountryPickerFlowLikeRxPresenter
import id.djaka.droidjam.common.ui.country_picker.CountryPickerScreenFlowLikeRx

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            App()
//            CountryPickerScreenRx()
            CountryPickerScreenFlowLikeRx()
        }
    }
}