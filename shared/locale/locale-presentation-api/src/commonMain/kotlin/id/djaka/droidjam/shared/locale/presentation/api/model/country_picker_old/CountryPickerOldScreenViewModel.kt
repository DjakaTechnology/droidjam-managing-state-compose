package id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_old

import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.item.CountryPickerItem
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_rx.CountryPickerRxModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class CountryPickerOldScreenViewModel {
    val initialState = MutableStateFlow(listOf<CountryPickerItem>())

    val query = MutableStateFlow("")

    val filteredCountryCodeList = MutableStateFlow(listOf<CountryPickerItem>())

    val selectedCountry = MutableStateFlow<CountryCodeModel?>(null)

    val isLoading = MutableStateFlow(false)

    val selectedCountryDisplay = selectedCountry.map {
        if (it != null) "Selected " + it.name else null
    }
}

class CountryPickerOldScreenViewModelReactive {
    val initialState = MutableStateFlow<CountryPickerRxModel.CountryListState>(CountryPickerRxModel.CountryListState.Loading)

    val query = MutableStateFlow("")

    val filteredCountryCodeList = MutableStateFlow<CountryPickerRxModel.CountryListState>(CountryPickerRxModel.CountryListState.Loading)

    val selectedCountry = MutableStateFlow<CountryCodeModel?>(null)

    val isLoading = combine(query, filteredCountryCodeList, initialState) { query, filteredCountryCodeList, initialState ->
        if (query.isEmpty()) {
            initialState is CountryPickerRxModel.CountryListState.Loading
        } else {
            filteredCountryCodeList is CountryPickerRxModel.CountryListState.Loading
        }
    }

    val result = combine(query, filteredCountryCodeList, initialState) { query, filteredCountry, initialState ->
        if (query.isEmpty()) filteredCountry else initialState
    }
}