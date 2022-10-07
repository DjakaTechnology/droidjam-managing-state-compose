package id.djaka.droidjam.shared.locale.ui.country_picker_old

import id.djaka.droidjam.shared.locale.app.model.CountryCodeModel
import id.djaka.droidjam.shared.locale.app.presenter.country_picker.item.CountryPickerItem
import kotlinx.coroutines.flow.MutableStateFlow
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