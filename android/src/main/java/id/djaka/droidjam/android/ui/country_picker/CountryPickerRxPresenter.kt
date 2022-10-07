package id.djaka.droidjam.android.ui.country_picker

import id.djaka.droidjam.common.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.common.domain.SearchCountryCodeFilterUseCase
import id.djaka.droidjam.common.domain.SearchCountryUseCases
import id.djaka.droidjam.common.ui.country_picker.CountryPickerEvent
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem
import id.djaka.droidjam.common.ui.country_picker.variant.CountryPickerRxModel
import id.djaka.droidjam.common.ui.country_picker.variant.CountryPickerRxResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import kotlinx.coroutines.rx3.asObservable
import kotlinx.coroutines.rx3.rxSingle
import java.util.concurrent.TimeUnit

class CountryPickerRxPresenter(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
) {
    fun presentRx(
        events: Observable<CountryPickerEvent>
    ): Observable<CountryPickerRxModel> = Observable.merge(
        searchCountryUseCases
            .getSearchCountryCodeInitialStateFlow()
            .asObservable()
            .map { CountryPickerRxResult.InitialStateLoad(CountryPickerRxModel.CountryListState.Success(it)) }
            .startWith(Observable.just(CountryPickerRxResult.InitialStateLoad(CountryPickerRxModel.CountryListState.Loading))),

        events.filter { it is CountryPickerEvent.SearchBoxChanged }
            .map { it as CountryPickerEvent.SearchBoxChanged }
            .switchMap {
                Observable.merge(
                    Observable.just(it.query)
                        .delay(200, TimeUnit.MILLISECONDS)
                        .filter { it.isNotEmpty() }
                        .compose(composeCountryListChangeResult()),
                    Observable.just(CountryPickerRxResult.SearchBoxQueryChange(it.query)),
                )
            },

        events.filter { it is CountryPickerEvent.ItemClicked }
            .map { it as CountryPickerEvent.ItemClicked }
            .doOnNext { saveRecentCountryUseCase(it.item.code) }
            .map { CountryPickerRxResult.SelectCountry(it.item) }

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
    }

    private fun composeCountryListChangeResult() = ObservableTransformer {
        it.compose(transformFilterCountry()).map {
            CountryPickerRxResult.SearchStateChange(it)
        }.startWith(
            Observable.just(CountryPickerRxResult.SearchStateChange(CountryPickerRxModel.CountryListState.Loading))
        )
    }

    private fun transformInitialState(): ObservableTransformer<List<CountryPickerItem>, CountryPickerRxModel.CountryListState> = ObservableTransformer {
        it.map {
            if (it.isEmpty()) {
                CountryPickerRxModel.CountryListState.Loading
            } else {
                CountryPickerRxModel.CountryListState.Success(it)
            }
        }
    }

    private fun transformFilterCountry(): ObservableTransformer<String, CountryPickerRxModel.CountryListState> = ObservableTransformer { queryObservable ->
        queryObservable.flatMap { it ->
            Observable.zip(
                Observable.just(it),
                searchCountryUseCases.searchCountryCodeFilter.invokeRx(it)
            ) { query, result ->
                Pair(query, result)
            }
        }.map { (query, result) ->
            if (result.isEmpty()) {
                CountryPickerRxModel.CountryListState.Empty("Sorry, we can't found country with \"$query\"")
            } else {
                CountryPickerRxModel.CountryListState.Success(result)
            }
        }.startWith(Observable.just(CountryPickerRxModel.CountryListState.Loading))
    }
}

fun SearchCountryCodeFilterUseCase.invokeRx(query: String): Observable<List<CountryPickerItem.Picker>> =
    Observable.just(query)
        .flatMapSingle {
            rxSingle { invoke(it) }
        }