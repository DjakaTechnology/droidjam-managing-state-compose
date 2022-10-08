package id.djaka.droidjam.shared.locale.app.domain

import id.djaka.droidjam.shared.core.framework.dispatcher.CoroutineDispatchers
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.item.CountryPickerItem
import kotlinx.coroutines.withContext

class SearchCountryCodeFilterUseCase(
    private val countryCodeRepository: CountryCodeRepository,
    private val convertCountryCodeModelToCountryPickerItem: CountryPickerConverter,
    private val dispatchers: CoroutineDispatchers
) {
    suspend operator fun invoke(query: String): List<CountryPickerItem.Picker> {
        return withContext(dispatchers.default()) {
            countryCodeRepository.getSortedCountryCode()
                .map { convertCountryCodeModelToCountryPickerItem.convert(it) }
                .filter {
                    it.item.name.startsWith(query, ignoreCase = true)
                            || it.item.code.startsWith(query, ignoreCase = true)
                }
        }
    }
}