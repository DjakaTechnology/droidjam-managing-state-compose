package id.djaka.droidjam.android.ui.country_picker

import id.djaka.droidjam.shared.locale.app.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryCodeFilterUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryUseCases
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerEvent
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.item.CountryPickerItem
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_rx.CountryPickerRxModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_rx.CountryPickerRxResult
import io.reactivex.rxjava3.core.Observable
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
            .getSearchCountryCodeInitialStateFlow().asObservable()
            .startWith(Observable.just(listOf()))
            .map {
                if (it.isEmpty()) {
                    CountryPickerRxResult.InitialStateLoad(CountryPickerRxModel.CountryListState.Loading)
                } else {
                    CountryPickerRxResult.InitialStateLoad(CountryPickerRxModel.CountryListState.Success(it))
                }
            },

        events.filter { it is CountryPickerEvent.SearchBoxChanged }
            .map { it as CountryPickerEvent.SearchBoxChanged }
            .switchMap {
                Observable.merge(
                    filterCountry(it.query).map { CountryPickerRxResult.SearchStateChange(it) },
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


    private fun filterCountry(query: String) =
        Observable.just(query)
            .filter { it.isNotEmpty() }
            .switchMap { searchCountryUseCases.searchCountryCodeFilter.invokeRx(query) }
            .map { result ->
                if (result.isEmpty()) {
                    CountryPickerRxModel.CountryListState.Empty("Sorry, we can't found country with \"$query\"")
                } else {
                    CountryPickerRxModel.CountryListState.Success(result)
                }
            }.startWith(Observable.just(CountryPickerRxModel.CountryListState.Loading))
            .delaySubscription(200, TimeUnit.MILLISECONDS)
}

fun SearchCountryCodeFilterUseCase.invokeRx(query: String): Observable<List<CountryPickerItem.Picker>> =
    Observable.just(query)
        .flatMapSingle {
            rxSingle { invoke(it) }
        }