package id.djaka.droidjam.common.ui.country_picker

import androidx.compose.runtime.Immutable
import id.djaka.droidjam.common.model.CountryCodeModel

@Immutable
sealed class CountryPickerEvent {
    class SearchBoxChanged(val query: String) : CountryPickerEvent()
    class ItemClicked(val item: CountryCodeModel) : CountryPickerEvent()
}