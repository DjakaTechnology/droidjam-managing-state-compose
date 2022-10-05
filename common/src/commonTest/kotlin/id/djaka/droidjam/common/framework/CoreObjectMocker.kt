package id.djaka.droidjam.common.framework

import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.ui.country_picker.item.CountryNumberModel
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem

object CoreObjectMocker {
    fun mockCountryPickerItemPicker(
        item: CountryCodeModel = mockCountryCodeModel()
    ): CountryPickerItem.Picker {
        return CountryPickerItem.Picker(
            item
        )
    }
    fun mockCountryCodeModel(
        name: String = "name",
        code: String = "code",
        emoji: String = "emoji",
        unicode: String = "unicode",
        image: String = "image"
    ): CountryCodeModel {
        return CountryCodeModel(
            name = name,
            code = code,
            emoji = emoji,
            unicode = unicode,
            image = image
        )
    }
}