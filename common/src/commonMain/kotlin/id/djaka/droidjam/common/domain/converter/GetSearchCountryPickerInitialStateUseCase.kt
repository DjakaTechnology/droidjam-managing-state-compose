package id.djaka.droidjam.common.domain.converter

import id.djaka.droidjam.common.domain.CountryPickerConverter
import id.djaka.droidjam.common.repository.CountryCodeRepository
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetSearchCountryPickerInitialStateUseCase(
    private val countryCodeRepository: CountryCodeRepository,
    private val countryPickerConverter: CountryPickerConverter
) {
    operator fun invoke(): Flow<List<CountryPickerItem>> {
        print("Country Picker generated flow")
        return countryCodeRepository.getRecentCountryCode().map { it ->
            delay(500) // SIMULATE API CALL

            val result = mutableListOf<CountryPickerItem>()
            val defaultCountryCode = countryPickerConverter.convert(countryCodeRepository.getDefaultCountryCode())
            val recentSearch = it.take(MAX_RECENT_SEARCH_SHOWN).map { countryPickerConverter.convert(it) }
            val allCountryCode = countryCodeRepository.getSortedCountryCode().map { countryPickerConverter.convert(it) }

            result.add(defaultCountryCode)

            if (recentSearch.isNotEmpty()) {
                result.add(CountryPickerItem.Header("Recent Search"))
                result.addAll(recentSearch)
            }

            result.add(CountryPickerItem.Divider)

            result.addAll(allCountryCode)
            result
        }.flowOn(Dispatchers.Default)
    }

    companion object {
        const val MAX_RECENT_SEARCH_SHOWN = 3
    }
}