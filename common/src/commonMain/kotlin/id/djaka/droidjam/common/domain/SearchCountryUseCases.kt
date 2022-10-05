package id.djaka.droidjam.common.domain

import id.djaka.droidjam.common.domain.converter.GetSearchCountryPickerInitialStateUseCase

class SearchCountryUseCases(
    val searchCountryCodeFilter: SearchCountryCodeFilterUseCase,
    val getSearchCountryCodeInitialStateFlow: GetSearchCountryPickerInitialStateUseCase
)