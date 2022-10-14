package id.djaka.droidjam.shared.locale.app.presenter.country_picker.variant

import id.djaka.droidjam.shared.core.framework.Presenter
import id.djaka.droidjam.shared.locale.app.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryUseCases
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerEvent
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_rx.CountryPickerRxModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_rx.CountryPickerRxResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class CountryPickerFlowLikeRxPresenter(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
) : Presenter<CountryPickerEvent, CountryPickerRxModel> {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun present(
        coroutineScope: CoroutineScope,
        event: Flow<CountryPickerEvent>,
    ): StateFlow<CountryPickerRxModel> {
        return merge<CountryPickerRxResult>(
            combine(
                searchCountryUseCases.getSearchCountryCodeInitialStateFlow().onStart { emit(listOf()) },
                event.filterIsInstance<CountryPickerEvent.SearchBoxChanged>().onStart { emit(CountryPickerEvent.SearchBoxChanged("")) },
                ::Pair
            ).filter { it.second.query.isEmpty() }
                .map { (initialState, event) ->
                    if (initialState.isEmpty()) {
                        CountryPickerRxResult.SearchStateChange(event.query, CountryPickerRxModel.CountryListState.Loading)
                    } else {
                        CountryPickerRxResult.SearchStateChange(event.query, CountryPickerRxModel.CountryListState.Success(initialState))
                    }
                },

            event.filterIsInstance<CountryPickerEvent.SearchBoxChanged>().transformLatest {
                if (it.query.isEmpty()) return@transformLatest

                emit(CountryPickerRxResult.SearchStateChange(it.query, CountryPickerRxModel.CountryListState.Loading))

                delay(200) // Debounce
                emit(CountryPickerRxResult.SearchStateChange(it.query, filterCountry(it.query)))
            },

            event.filterIsInstance<CountryPickerEvent.ItemClicked>()
                .onEach { saveRecentCountryUseCase(it.item.code) }
                .transform {
                    emit(CountryPickerRxResult.SelectCountry(it.item))
                },

            ).scan(CountryPickerRxModel.empty()) { state, result ->

            when (result) {
                is CountryPickerRxResult.SelectCountry -> state.copy(selectedCountry = result.countryCodeModel)

                is CountryPickerRxResult.SearchStateChange -> {
                    state.copy(result.query, result.state)
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