package id.djaka.droidjam.common.ui.country_picker.item

import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.util.Parcelable
import id.djaka.droidjam.common.util.Parcelize

sealed class CountryPickerItem : Parcelable {
    @Parcelize
    class Picker(val item: CountryCodeModel) : CountryPickerItem()

    @Parcelize
    class Header(val header: String) : CountryPickerItem()

    @Parcelize
    object Divider : CountryPickerItem()
}