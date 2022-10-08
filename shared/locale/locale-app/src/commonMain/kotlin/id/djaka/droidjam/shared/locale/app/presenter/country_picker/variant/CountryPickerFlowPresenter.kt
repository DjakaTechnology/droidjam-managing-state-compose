package id.djaka.droidjam.shared.locale.app.presenter.country_picker.variant

import id.djaka.droidjam.shared.core.framework.Presenter
import id.djaka.droidjam.shared.locale.app.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryUseCases
import id.djaka.droidjam.shared.locale.app.presenter.country_picker.CountryPickerPresenter
import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerEvent
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.item.CountryPickerItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CountryPickerFlowPresenter(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
): Presenter<CountryPickerEvent, CountryPickerModel> {
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
        presentCountryListStateFlow(queryFlow).onStart { emit(CountryPickerModel.CountryListState.Loading) }
    ) { query, selectedCountry, countryStateFlow ->
        CountryPickerModel(
            searchBox = query,
            countryListState = countryStateFlow,
            selectedCountry = selectedCountry
        )
    }.stateIn(
        coroutineScope, SharingStarted.WhileSubscribed(5000),
        CountryPickerModel.empty()
    )

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun presentCountryListStateFlow(
        queryFlow: StateFlow<String>
    ): Flow<CountryPickerModel.CountryListState> =
        combine(
            queryFlow.debounce(200),
            searchCountryUseCases.getSearchCountryCodeInitialStateFlow()
        ) { query, stateFlow ->
            Pair(query, stateFlow)
        }.flatMapLatest { (query, initialState) ->
            if (query.isEmpty()) {
                composeInitialState(initialState)
            } else {
                composeFilterResultState(query)
            }
        }

    private fun composeFilterResultState(query: String) = flow {
        emit(CountryPickerModel.CountryListState.Loading)
        println("Result: Fetching")
        emit(filterCountry(query))
        println("Result: updated")
    }.conflate()

    private fun composeInitialState(initialState: List<CountryPickerItem>): Flow<CountryPickerModel.CountryListState> = flow {
        if (initialState.isEmpty()) {
            emit(CountryPickerModel.CountryListState.Loading)
        } else {
            emit(CountryPickerModel.CountryListState.Success(initialState))
        }
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