package id.djaka.droidjam.common.ui.country_picker.variant

import id.djaka.droidjam.common.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.common.domain.SearchCountryUseCases
import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.ui.country_picker.CountryPickerEvent
import id.djaka.droidjam.common.ui.country_picker.CountryPickerPresenter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class CountryPickerFlowLikeRxPresenter(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun presentFlow(
        coroutineScope: CoroutineScope,
        event: Flow<CountryPickerEvent>,
    ): StateFlow<CountryPickerRxModel> {
        return merge(
            searchCountryUseCases.getSearchCountryCodeInitialStateFlow().onStart { emit(listOf()) }
                .transform { initialState ->
                    if (initialState.isEmpty()) {
                        emit(CountryPickerRxResult.InitialStateLoad(CountryPickerRxModel.CountryListState.Loading))
                    } else {
                        emit(CountryPickerRxResult.InitialStateLoad(CountryPickerRxModel.CountryListState.Success(initialState)))
                    }
                },

            event.filterIsInstance<CountryPickerEvent.SearchBoxChanged>().transformLatest {
                emit(CountryPickerRxResult.SearchBoxQueryChange(it.query))

                if (it.query.isNotEmpty()) {
                    emit(CountryPickerRxResult.SearchStateChange(CountryPickerRxModel.CountryListState.Loading))
                    delay(200) // Debounce
                    println("Result: Fetching")
                    emit(CountryPickerRxResult.SearchStateChange(filterCountry(it.query)))
                    println("Result: updated")
                }
            },

            event.filterIsInstance<CountryPickerEvent.ItemClicked>()
                .onEach { saveRecentCountryUseCase(it.item.code) }
                .transform {
                    emit(CountryPickerRxResult.SelectCountry(it.item))
                    emit(CountryPickerRxResult.SearchBoxQueryChange(""))
                },

            ).scan(CountryPickerRxModel.empty()) { state, result ->

            when (result) {
                is CountryPickerRxResult.SelectCountry -> state.copy(selectedCountry = result.countryCodeModel)
                is CountryPickerRxResult.SearchBoxQueryChange -> {
                    if (result.query.isEmpty()) {
                        state.copy(searchBox = result.query, countryListState = state.initialList)
                    } else {
                        state.copy(searchBox = result.query)
                    }
                }

                is CountryPickerRxResult.SearchStateChange -> {
                    if (state.searchBox.isNotEmpty()) {
                        state.copy(countryListState = result.state)
                    } else state
                }

                is CountryPickerRxResult.InitialStateLoad -> {
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
        }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(5000), CountryPickerRxModel.empty())
    }

    private suspend fun filterCountry(query: String): CountryPickerRxModel.CountryListState {
        val filterResult = searchCountryUseCases.searchCountryCodeFilter(query)
        return if (filterResult.isEmpty()) {
            CountryPickerRxModel.CountryListState.Empty("Sorry, we can't found country with \"$query\"")
        } else {
            CountryPickerRxModel.CountryListState.Success(filterResult)
        }
    }




}