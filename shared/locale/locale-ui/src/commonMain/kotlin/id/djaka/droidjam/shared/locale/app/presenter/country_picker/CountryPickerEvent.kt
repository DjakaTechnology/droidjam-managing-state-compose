package id.djaka.droidjam.shared.locale.app.presenter.country_picker

import androidx.compose.runtime.Immutable
import id.djaka.droidjam.shared.locale.app.model.CountryCodeModel

@Immutable
sealed class CountryPickerEvent {
    class SearchBoxChanged(val query: String) : CountryPickerEvent()
    class ItemClicked(val item: CountryCodeModel) : CountryPickerEvent()
}