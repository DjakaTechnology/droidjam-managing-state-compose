package id.djaka.droidjam.shared.locale.app.presenter.country_picker.variant

import id.djaka.droidjam.shared.locale.app.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryUseCases
import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerEvent
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerModel
import id.djaka.droidjam.shared.locale.presentation.api.presenter.CountryPickerPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

class CountryPickerFlowPresenter(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
) : CountryPickerPresenter {
    override fun present(
        coroutineScope: CoroutineScope,
        event: Flow<CountryPickerEvent>,
    ): StateFlow<CountryPickerModel> {
        val query = MutableStateFlow("")
        val selectedCountry = MutableStateFlow<CountryCodeModel?>(null)

        coroutineScope.launch {
            event.collect {
                when (it) {
                    is CountryPickerEvent.ItemClicked -> {
                        selectedCountry.value = it.item
                        saveRecentCountryUseCase(it.item.code)
                        query.value = ""
                    }

                    is CountryPickerEvent.SearchBoxChanged -> query.value = it.query
                }
            }
        }

        return uiState(coroutineScope, query, selectedCountry)
    }

    private fun uiState(
        coroutineScope: CoroutineScope,
        queryFlow: StateFlow<String>,
        selectedCountryFlow: StateFlow<CountryCodeModel?>
    ) = combine(
        queryFlow,
        selectedCountryFlow,
        filterStateFlow(queryFlow).onStart { emit(CountryPickerModel.CountryListState.Loading) },
        initialState().onStart { emit(CountryPickerModel.CountryListState.Loading) },
    ) { query, selectedCountry, countryStateFlow, initialState ->
        CountryPickerModel(
            searchBox = query,
            countryListState = if (query.isEmpty()) initialState else countryStateFlow,
            selectedCountry = selectedCountry
        )
    }.stateIn(
        coroutineScope, SharingStarted.WhileSubscribed(5000),
        CountryPickerModel.empty()
    )

    private fun initialState() = searchCountryUseCases.getSearchCountryCodeInitialStateFlow()
        .map { initialState ->
            if (initialState.isEmpty()) {
                CountryPickerModel.CountryListState.Loading
            } else {
                CountryPickerModel.CountryListState.Success(initialState)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun filterStateFlow(queryFlow: StateFlow<String>) = queryFlow
        .filter { it.isNotEmpty() }
        .transformLatest {
            emit(CountryPickerModel.CountryListState.Loading)

            delay(200)
            emit(filterCountry(it))
        }

    private suspend fun filterCountry(query: String): CountryPickerModel.CountryListState {
        val filterResult = searchCountryUseCases.searchCountryCodeFilter(query)
        return if (filterResult.isEmpty()) {
            CountryPickerModel.CountryListState.Empty("Sorry, we can't found country with \"$query\"")
        } else {
            CountryPickerModel.CountryListState.Success(filterResult)
        }
    }
}

