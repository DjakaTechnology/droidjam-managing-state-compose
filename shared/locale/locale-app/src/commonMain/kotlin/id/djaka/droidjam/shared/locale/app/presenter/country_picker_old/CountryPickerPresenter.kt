package id.djaka.droidjam.shared.locale.app.presenter.country_picker_old

import id.djaka.droidjam.shared.locale.app.repository.CountryCodeRepository
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryUseCases
import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_old.CountryPickerOldScreenState
import id.djaka.droidjam.shared.locale.presentation.api.presenter.CountryPickerScreenAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CountryPickerPresenter constructor(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val countryCodeRepository: CountryCodeRepository,
    private val coroutineScope: CoroutineScope,
) : CountryPickerScreenAction {
    val state = CountryPickerOldScreenState()

    init {
        initInitialState()
    }

    private fun initInitialState() {
        coroutineScope.launch {
            state.isLoading.value = true

            searchCountryUseCases.getSearchCountryCodeInitialStateFlow().collect {
                state.initialState.value = it

                state.isLoading.value = false // Might cause race condition bug
            }
        }
    }

    override fun onSearchBoxChanged(query: String) {
        state.query.value = query

        loadSearch(query)
    }

    var searchJob: Job? = null
    private fun loadSearch(query: String) {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            state.isLoading.value = true

            if (query.isEmpty()) {
                state.filteredCountryCodeList.value = state.initialState.value
            } else {
                searchCountryUseCases.searchCountryCodeFilter(query)
            }

            state.isLoading.value = false
        }
    }

    override fun onItemClicked(item: CountryCodeModel) {
        coroutineScope.launch {
            countryCodeRepository.saveRecentCountryCode(item.code)
            state.selectedCountry.value = item
            loadSearch("")
        }
    }
}