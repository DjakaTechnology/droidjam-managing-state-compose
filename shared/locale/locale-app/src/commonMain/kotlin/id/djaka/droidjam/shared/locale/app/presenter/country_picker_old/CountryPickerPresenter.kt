package id.djaka.droidjam.shared.locale.app.presenter.country_picker_old

import id.djaka.droidjam.shared.locale.app.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryUseCases
import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_old.CountryPickerOldScreenViewModel
import id.djaka.droidjam.shared.locale.presentation.api.presenter.CountryPickerScreenAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CountryPickerPresenter constructor(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
    private val coroutineScope: CoroutineScope,
) : CountryPickerScreenAction {
    val viewModel = CountryPickerOldScreenViewModel()

    init {
        initInitialState()
    }

    private fun initInitialState() {
        coroutineScope.launch {
            viewModel.isLoading.value = true

            searchCountryUseCases.getSearchCountryCodeInitialStateFlow().collect {
                viewModel.initialState.value = it
                viewModel.filteredCountryCodeList.value = viewModel.initialState.value

                viewModel.isLoading.value = false // Might cause race condition bug
            }
        }
    }

    override fun onSearchBoxChanged(query: String) {
        viewModel.query.value = query

        loadSearch(query)
    }

    var searchJob: Job? = null
    private fun loadSearch(query: String) {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            viewModel.isLoading.value = true

            if (query.isEmpty()) {
                viewModel.filteredCountryCodeList.value = viewModel.initialState.value
            } else {
                viewModel.filteredCountryCodeList.value = searchCountryUseCases.searchCountryCodeFilter(query)
            }

            viewModel.isLoading.value = false
        }
    }

    override fun onItemClicked(item: CountryCodeModel) {
        coroutineScope.launch {
            saveRecentCountryUseCase(item.code)
            viewModel.selectedCountry.value = item
            loadSearch("")
        }
    }
}