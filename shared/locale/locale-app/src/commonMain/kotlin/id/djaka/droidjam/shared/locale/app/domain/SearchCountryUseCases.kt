package id.djaka.droidjam.shared.locale.app.domain

class SearchCountryUseCases(
    val searchCountryCodeFilter: SearchCountryCodeFilterUseCase,
    val getSearchCountryCodeInitialStateFlow: GetSearchCountryPickerInitialStateUseCase
)