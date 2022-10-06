package id.djaka.droidjam.common.ui.country_picker

import id.djaka.droidjam.common.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.common.domain.SearchCountryUseCases
import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.ui.country_picker.CountryPickerPresenter.Event
import id.djaka.droidjam.common.ui.country_picker.CountryPickerPresenter.UIState
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class CountryPickerFlowLikeRxPresenter(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun presentFlow(
        coroutineScope: CoroutineScope,
        event: Flow<Event>,
    ): StateFlow<UIState> {
        return merge(
            searchCountryUseCases.getSearchCountryCodeInitialStateFlow()
                .flatMapLatest { initialState ->
                    merge(
                        event.filterIsInstance<Event.SearchBoxChanged>()
                            .flatMapLatest { searchBoxChanged(it.query, initialState) },
                        flowOf(
                            Result.InitialStateLoad(UIState.CountryListState.Success(initialState))
                        )
                    )
                },

            event.filterIsInstance<Event.ItemClicked>()
                .onEach { saveRecentCountryUseCase(it.item.code) }
                .map { Result.SelectCountry(it.item) },

            ).scan(UIState.empty()) { state, result ->
            when (result) {
                is Result.SelectCountry -> state.copy(selectedCountry = result.countryCodeModel)
                is Result.SearchBoxQueryChange -> state.copy(searchBox = result.query)
                is Result.SearchStateChange -> state.copy(countryListState = result.state)
                is Result.InitialStateLoad -> if (state.searchBox.isEmpty()) {
                    state.copy(countryListState = result.state)
                } else state
            }
        }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(5000), UIState.empty())
    }

    private fun searchBoxChanged(
        query: String,
        initialState: List<CountryPickerItem>
    ) = flow {
        emit(Result.SearchBoxQueryChange(query))
        delay(200)
        handleSearchStateFlow(query, initialState).collect {
            emit(Result.SearchStateChange(it))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleSearchStateFlow(query: String, initialState: List<CountryPickerItem>) = flowOf(Unit).flatMapLatest {
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
        delay(1000) // Simulate Api call

        val filterResult = searchCountryUseCases.searchCountryCodeFilter(query)
        return if (filterResult.isEmpty()) {
            UIState.CountryListState.Empty("Sorry, we can't found country with \"$query\"")
        } else {
            UIState.CountryListState.Success(filterResult)
        }
    }

    sealed class Result {
        class SearchBoxQueryChange(
            val query: String,
        ) : Result()

        class SearchStateChange(
            val state: UIState.CountryListState
        ) : Result()

        class SelectCountry(
            val countryCodeModel: CountryCodeModel
        ) : Result()

        class InitialStateLoad(
            val state: UIState.CountryListState
        ) : Result()
    }
}