package id.djaka.droidjam.common.ui.country_picker_old

import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class CountryPickerScreenState {
    val initialState = MutableStateFlow(listOf<CountryPickerItem>())

    val query = MutableStateFlow("")

    val filteredCountryCodeList = MutableStateFlow(listOf<CountryPickerItem>())

    val selectedCountry = MutableStateFlow<CountryCodeModel?>(null)

    val isLoading = MutableStateFlow(false)

    val selectedCountryDisplay = selectedCountry.map {
        if (it != null) "Selected " + it.name else null
    }
}