package id.djaka.droidjam.shared.locale.app.domain

import id.djaka.droidjam.shared.locale.app.repository.CountryCodeRepository
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.item.CountryPickerItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetSearchCountryPickerInitialStateUseCase(
    private val countryCodeRepository: CountryCodeRepository,
    private val countryPickerConverter: CountryPickerConverter
) {
    operator fun invoke(): Flow<List<CountryPickerItem>> {
        print("Country Picker generated flow")
        return combine(
            countryCodeRepository.getRecentCountryCodeFlow(),
            countryCodeRepository.getDefaultCountryCodeFlow(),
            flow { emit(countryCodeRepository.getSortedCountryCode()) },
        ) { recentCountryCode, defaultCountry, sortedCountryCode ->
            val result = mutableListOf<CountryPickerItem>()
            val defaultCountryCode = countryPickerConverter.convert(defaultCountry)
            val recentSearch = recentCountryCode.take(MAX_RECENT_SEARCH_SHOWN).map { countryPickerConverter.convert(it) }
            val allCountryCode = sortedCountryCode.map { countryPickerConverter.convert(it) }

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