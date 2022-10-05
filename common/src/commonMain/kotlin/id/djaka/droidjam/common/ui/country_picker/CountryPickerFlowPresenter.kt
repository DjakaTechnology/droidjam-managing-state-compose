package id.djaka.droidjam.common.ui.country_picker

import id.djaka.droidjam.common.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.common.domain.SearchCountryUseCases
import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.ui.country_picker.CountryPickerPresenter.Event
import id.djaka.droidjam.common.ui.country_picker.CountryPickerPresenter.UIState
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class CountryPickerFlowPresenter(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
) {
    fun presentFlow(
        coroutineScope: CoroutineScope,
        event: Flow<Event>,
    ): StateFlow<UIState> {
        val query = MutableStateFlow("")
        val selectedCountry = MutableStateFlow<CountryCodeModel?>(null)

        coroutineScope.launch {
            event.collect {
                when (it) {
                    is Event.ItemClicked -> {
                        selectedCountry.value = it.item
                        saveRecentCountryUseCase(it.item.code)
                        query.value = ""
                    }

                    is Event.SearchBoxChanged -> query.value = it.query
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
        presentCountryListStateFlow(queryFlow).onStart { emit(UIState.CountryListState.Loading) }
    ) { query, selectedCountry, countryStateFlow ->
        UIState(
            searchBox = query,
            countryListState = countryStateFlow,
            selectedCountry = selectedCountry
        )
    }.stateIn(
        coroutineScope, SharingStarted.WhileSubscribed(5000),
        UIState.empty()
    )

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun presentCountryListStateFlow(
        queryFlow: StateFlow<String>
    ): Flow<UIState.CountryListState> =
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
        emit(UIState.CountryListState.Loading)
        println("Result: Fetching")
        emit(filterCountry(query))
        println("Result: updated")
    }.conflate()

    private fun composeInitialState(initialState: List<CountryPickerItem>): Flow<UIState.CountryListState> = flow {
        if (initialState.isEmpty()) {
            emit(UIState.CountryListState.Loading)
        } else {
            emit(UIState.CountryListState.Success(initialState))
        }
    }

    private suspend fun filterCountry(query: String): UIState.CountryListState {
        delay(2000) // Simulate Api call

        val filterResult = searchCountryUseCases.searchCountryCodeFilter(query)
        return if (filterResult.isEmpty()) {
            UIState.CountryListState.Empty("Sorry, we can't found country with \"$query\"")
        } else {
            UIState.CountryListState.Success(filterResult)
        }
    }
}