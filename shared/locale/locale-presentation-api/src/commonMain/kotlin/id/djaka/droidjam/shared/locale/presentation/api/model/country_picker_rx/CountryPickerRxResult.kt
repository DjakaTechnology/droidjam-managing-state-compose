package id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_rx

import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel

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