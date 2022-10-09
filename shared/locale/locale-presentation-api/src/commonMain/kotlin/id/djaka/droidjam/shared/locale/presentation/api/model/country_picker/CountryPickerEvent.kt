package id.djaka.droidjam.shared.locale.presentation.api.model.country_picker

import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel

sealed interface CountryPickerEvent {
    class SearchBoxChanged(val query: String) : CountryPickerEvent
    class ItemClicked(val item: CountryCodeModel) : CountryPickerEvent
}