package id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_rx

import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.item.CountryPickerItem

data class CountryPickerRxModel(
    val searchBox: String,
    val countryListState: CountryListState,
    val selectedCountry: CountryCodeModel? = null,
) {
    sealed class CountryListState {
        object Loading : CountryListState()
        data class Success(val countryCodes: List<CountryPickerItem>) : CountryListState()
        data class Empty(val message: String) : CountryListState()
    }

    companion object {
        fun empty() = CountryPickerRxModel(
            searchBox = "",
            countryListState = CountryListState.Loading,
            selectedCountry = null,
        )
    }
}

fun CountryPickerRxModel.toGenericModel() = CountryPickerModel(
    searchBox = searchBox,
    countryListState = countryListState.toGenericModel(),
    selectedCountry = selectedCountry
)

fun CountryPickerRxModel.CountryListState.toGenericModel() = when (this) {
    is CountryPickerRxModel.CountryListState.Empty -> CountryPickerModel.CountryListState.Empty(message)
    CountryPickerRxModel.CountryListState.Loading -> CountryPickerModel.CountryListState.Loading
    is CountryPickerRxModel.CountryListState.Success -> CountryPickerModel.CountryListState.Success(countryCodes)
}