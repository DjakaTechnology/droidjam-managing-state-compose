package id.djaka.droidjam.common.ui.country_picker.variant

import id.djaka.droidjam.common.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.common.domain.SearchCountryUseCases
import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.ui.country_picker.CountryPickerEvent
import id.djaka.droidjam.common.ui.country_picker.CountryPickerPresenter.Model
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class CountryPickerFlowPresenter(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
) {
    fun presentFlow(
        coroutineScope: CoroutineScope,
        event: Flow<CountryPickerEvent>,
    ): StateFlow<Model> {
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
        presentCountryListStateFlow(queryFlow).onStart { emit(Model.CountryListState.Loading) }
    ) { query, selectedCountry, countryStateFlow ->
        Model(
            searchBox = query,
            countryListState = countryStateFlow,
            selectedCountry = selectedCountry
        )
    }.stateIn(
        coroutineScope, SharingStarted.WhileSubscribed(5000),
        Model.empty()
    )

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun presentCountryListStateFlow(
        queryFlow: StateFlow<String>
    ): Flow<Model.CountryListState> =
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
        emit(Model.CountryListState.Loading)
        println("Result: Fetching")
        emit(filterCountry(query))
        println("Result: updated")
    }.conflate()

    private fun composeInitialState(initialState: List<CountryPickerItem>): Flow<Model.CountryListState> = flow {
        if (initialState.isEmpty()) {
            emit(Model.CountryListState.Loading)
        } else {
            emit(Model.CountryListState.Success(initialState))
        }
    }

    private suspend fun filterCountry(query: String): Model.CountryListState {
        val filterResult = searchCountryUseCases.searchCountryCodeFilter(query)
        return if (filterResult.isEmpty()) {
            Model.CountryListState.Empty("Sorry, we can't found country with \"$query\"")
        } else {
            Model.CountryListState.Success(filterResult)
        }
    }
}