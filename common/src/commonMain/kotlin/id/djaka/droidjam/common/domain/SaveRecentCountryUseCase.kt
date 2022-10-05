package id.djaka.droidjam.common.domain

import id.djaka.droidjam.common.repository.CountryCodeRepository

class SaveRecentCountryUseCase(
    private val countryCodeRepository: CountryCodeRepository,
) {
    operator fun invoke(region: String) {
        countryCodeRepository.saveRecentCountryCode(region)
    }
}