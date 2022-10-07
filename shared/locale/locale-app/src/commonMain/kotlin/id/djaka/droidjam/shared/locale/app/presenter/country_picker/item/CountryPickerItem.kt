package id.djaka.droidjam.shared.locale.app.presenter.country_picker.item

import id.djaka.droidjam.shared.core.util.Parcelable
import id.djaka.droidjam.shared.core.util.Parcelize
import id.djaka.droidjam.shared.locale.app.model.CountryCodeModel

sealed class CountryPickerItem : Parcelable {
    @Parcelize
    class Picker(val item: CountryCodeModel) : CountryPickerItem()

    @Parcelize
    class Header(val header: String) : CountryPickerItem()

    @Parcelize
    object Divider : CountryPickerItem()
}