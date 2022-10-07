package id.djaka.droidjam.common.ui.country_picker.variant

import id.djaka.droidjam.common.model.CountryCodeModel

sealed class CountryPickerRxResult {
    class SearchBoxQueryChange(
        val query: String,
    ) : CountryPickerRxResult()

    class SearchStateChange(
        val state: CountryPickerRxModel.CountryListState
    ) : CountryPickerRxResult()

    class SelectCountry(
        val countryCodeModel: CountryCodeModel
    ) : CountryPickerRxResult()

    class InitialStateLoad(
        val state: CountryPickerRxModel.CountryListState
    ) : CountryPickerRxResult()
}