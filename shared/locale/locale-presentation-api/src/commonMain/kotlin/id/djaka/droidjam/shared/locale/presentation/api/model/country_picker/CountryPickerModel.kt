package id.djaka.droidjam.shared.locale.presentation.api.model.country_picker

import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.item.CountryPickerItem

data class CountryPickerModel(
    val searchBox: String,
    val countryListState: CountryListState,
    val selectedCountry: CountryCodeModel? = null
) {

    val selectedCountryDisplay = if (selectedCountry != null) "Selected ${selectedCountry.name}." else null

    val isLoading = countryListState is CountryListState.Loading

    val countryList = (countryListState as? CountryListState.Success)?.countryCodes

    val emptyStateMessage = (countryListState as? CountryListState.Empty)

    sealed class CountryListState {
        object Loading : CountryListState()
        data class Success(val countryCodes: List<CountryPickerItem>) : CountryListState()
        data class Empty(val message: String) : CountryListState()
    }

    companion object {
        fun empty() = CountryPickerModel(
            searchBox = "",
            countryListState = CountryListState.Loading,
            selectedCountry = null,
        )
    }
}