package id.djaka.droidjam.common.ui.country_picker

import androidx.compose.runtime.Immutable
import id.djaka.droidjam.common.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.common.domain.SearchCountryUseCases
import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.ui.country_picker.CountryPickerPresenter.Event
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
            searchCountryUseCases.getSearchCountryCodeInitialStateFlow().onStart { emit(listOf()) }
                .transform { initialState ->
                    if (initialState.isEmpty()) {
                        emit(Result.InitialStateLoad(UIState.CountryListState.Loading))
                    } else {
                        emit(Result.InitialStateLoad(UIState.CountryListState.Success(initialState)))
                    }
                },

            event.filterIsInstance<Event.SearchBoxChanged>().transformLatest {
                emit(Result.SearchBoxQueryChange(it.query))

                if (it.query.isNotEmpty()) {
                    emit(Result.SearchStateChange(UIState.CountryListState.Loading))
                    delay(200) // Debounce
                    println("Result: Fetching")
                    emit(Result.SearchStateChange(filterCountry(it.query)))
                    println("Result: updated")
                }
            },

            event.filterIsInstance<Event.ItemClicked>()
                .onEach { saveRecentCountryUseCase(it.item.code) }
                .transform {
                    emit(Result.SelectCountry(it.item))
                    emit(Result.SearchBoxQueryChange(""))
                },

            ).scan(UIState.empty()) { state, result ->

            when (result) {
                is Result.SelectCountry -> state.copy(selectedCountry = result.countryCodeModel)
                is Result.SearchBoxQueryChange -> {
                    if (result.query.isEmpty()) {
                        state.copy(searchBox = result.query, countryListState = state.initialList)
                    } else {
                        state.copy(searchBox = result.query)
                    }
                }

                is Result.SearchStateChange -> {
                    if (state.searchBox.isNotEmpty()) {
                        state.copy(countryListState = result.state)
                    } else state
                }

                is Result.InitialStateLoad -> {
                    if (state.searchBox.isEmpty()) {
                        state.copy(
                            countryListState = result.state,
                            initialList = result.state
                        )
                    } else {
                        state.copy(initialList = result.state)
                    }
                }
            }
        }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(5000), UIState.empty())
    }

    private suspend fun filterCountry(query: String): UIState.CountryListState {
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

    @Immutable
    data class UIState(
        val searchBox: String,
        val countryListState: CountryListState,
        val selectedCountry: CountryCodeModel? = null,
        val initialList: CountryListState
    ) {
        @Immutable
        sealed class CountryListState {
            object Loading : CountryListState()
            data class Success(val countryCodes: List<CountryPickerItem>) : CountryListState()
            data class Empty(val message: String) : CountryListState()
        }

        companion object {
            fun empty() = UIState(
                searchBox = "",
                countryListState = CountryListState.Loading,
                selectedCountry = null,
                initialList = CountryListState.Loading
            )
        }

    }
}

fun CountryPickerFlowLikeRxPresenter.UIState.toGenericUIState() = CountryPickerPresenter.UIState(
    searchBox = searchBox,
    countryListState = countryListState.toGenericUIState(),
    selectedCountry = selectedCountry
)

fun CountryPickerFlowLikeRxPresenter.UIState.CountryListState.toGenericUIState() = when (this) {
    is CountryPickerFlowLikeRxPresenter.UIState.CountryListState.Empty -> CountryPickerPresenter.UIState.CountryListState.Empty(message)
    CountryPickerFlowLikeRxPresenter.UIState.CountryListState.Loading -> CountryPickerPresenter.UIState.CountryListState.Loading
    is CountryPickerFlowLikeRxPresenter.UIState.CountryListState.Success -> CountryPickerPresenter.UIState.CountryListState.Success(countryCodes)
}