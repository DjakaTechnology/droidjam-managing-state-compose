package id.djaka.droidjam.shared.locale.app.domain

class SaveRecentCountryUseCase(
    private val countryCodeRepository: CountryCodeRepository,
) {
    operator fun invoke(region: String) {
        countryCodeRepository.saveRecentCountryCode(region)
    }
}