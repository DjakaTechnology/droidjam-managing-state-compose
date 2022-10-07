package id.djaka.droidjam.shared.locale.app.presenter.country_picker.variant

import id.djaka.droidjam.shared.locale.app.model.CountryCodeModel

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