package id.djaka.droidjam.common.ui.country_picker

import androidx.compose.runtime.Immutable
import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem

@Immutable
data class CountryPickerModel(
    val searchBox: String,
    val countryListState: CountryListState,
    val selectedCountry: CountryCodeModel? = null
) {

    val selectedCountryDisplay = if (selectedCountry != null) "Selected ${selectedCountry.name}." else null
    @Immutable
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