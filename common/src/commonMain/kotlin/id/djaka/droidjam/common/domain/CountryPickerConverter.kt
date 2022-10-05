package id.djaka.droidjam.common.domain

import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem

class CountryPickerConverter {
    fun convert(it: CountryCodeModel): CountryPickerItem.Picker {
        return CountryPickerItem.Picker(it)
    }
}