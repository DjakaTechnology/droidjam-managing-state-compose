package id.djaka.droidjam.shared.locale.app.domain

import id.djaka.droidjam.shared.locale.app.repository.CountryCodeRepository

class SaveRecentCountryUseCase(
    private val countryCodeRepository: CountryCodeRepository,
) {
    operator fun invoke(region: String) {
        countryCodeRepository.saveRecentCountryCode(region)
    }
}