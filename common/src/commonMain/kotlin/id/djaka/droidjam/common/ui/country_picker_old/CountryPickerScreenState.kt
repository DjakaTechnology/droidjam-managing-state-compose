package id.djaka.droidjam.common.ui.country_picker_old

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem

class CountryPickerScreenState {
    var initialState = listOf<CountryPickerItem>()

    var query by mutableStateOf("")

    var filteredCountryCodeList by mutableStateOf(listOf<CountryPickerItem>())

    var selectedCountry: CountryCodeModel? by mutableStateOf(null)

    var isLoading: Boolean by mutableStateOf(false)
}