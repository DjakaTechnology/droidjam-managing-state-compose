package id.djaka.droidjam.common.ui.country_picker.item

import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.util.Parcelable
import id.djaka.droidjam.common.util.Parcelize

@Parcelize
class CountryNumberModel constructor(
    val data: CountryCodeModel,
) : Parcelable