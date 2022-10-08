package id.djaka.droidjam.shared.locale.app.domain

import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.item.CountryPickerItem

class CountryPickerConverter {
    fun convert(it: CountryCodeModel): CountryPickerItem.Picker {
        return CountryPickerItem.Picker(it)
    }
}