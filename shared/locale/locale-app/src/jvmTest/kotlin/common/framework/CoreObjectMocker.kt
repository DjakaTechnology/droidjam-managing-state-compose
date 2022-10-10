package common.framework

import id.djaka.droidjam.shared.locale.app.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryUseCases
import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.item.CountryPickerItem
import io.mockk.every
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf

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

    fun mockSearchCountryUseCases(): SearchCountryUseCases {
        val useCase: SearchCountryUseCases = mockk(relaxed = true)
        every { useCase.getSearchCountryCodeInitialStateFlow() } returns flowOf(listOf())
        return useCase
    }

    fun mockSaveRecentCountryUseCase(): SaveRecentCountryUseCase {
        val useCase: SaveRecentCountryUseCase = mockk(relaxed = true)
        return useCase
    }
}