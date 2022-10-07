package id.djaka.droidjam.common.ui.country_picker_old

import id.djaka.droidjam.common.domain.SearchCountryUseCases
import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.repository.CountryCodeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CountryPickerPresenterOldWithFlow constructor(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val countryCodeRepository: CountryCodeRepository,
    private val coroutineScope: CoroutineScope,
) : CountryPickerScreenAction {
    val state = CountryPickerScreenState()

    init {
        initInitialState()
    }

    private fun initInitialState() {
        coroutineScope.launch {
            state.isLoading = true

            searchCountryUseCases.getSearchCountryCodeInitialStateFlow().collect {
                state.initialState = it

                state.isLoading = false // Might cause race condition bug
            }
        }
    }

    override fun onSearchBoxChanged(query: String) {
        state.query = query

        loadSearch(query)
    }

    var searchJob: Job? = null
    private fun loadSearch(query: String) {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            state.isLoading = true

            if (query.isEmpty()) {
                state.filteredCountryCodeList = state.initialState
            } else {
                searchCountryUseCases.searchCountryCodeFilter(query)
            }

            state.isLoading = false
        }
    }

    override fun onItemClicked(item: CountryCodeModel) {
        coroutineScope.launch {
            countryCodeRepository.saveRecentCountryCode(item.code)
            state.selectedCountry = item
        }
    }
}