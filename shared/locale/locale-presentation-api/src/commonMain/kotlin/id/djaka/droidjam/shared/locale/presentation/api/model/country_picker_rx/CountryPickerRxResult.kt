package id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_rx

import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel

sealed class CountryPickerRxResult {

    class SearchStateChange(
        val query: String,
        val state: CountryPickerRxModel.CountryListState
    ) : CountryPickerRxResult()

    class SelectCountry(
        val countryCodeModel: CountryCodeModel
    ) : CountryPickerRxResult()

}