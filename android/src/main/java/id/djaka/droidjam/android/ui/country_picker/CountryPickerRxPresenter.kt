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
        Observable.combineLatest(
            searchCountryUseCases.getSearchCountryCodeInitialStateFlow().asObservable()
                .startWithItem(listOf()),
            events.filter { it is CountryPickerEvent.SearchBoxChanged }
                .map { it as CountryPickerEvent.SearchBoxChanged }
                .startWithItem(CountryPickerEvent.SearchBoxChanged("")),
            ::Pair
        ).filter { it.second.query.isEmpty() }
            .map { (initialState, event) ->
                if (initialState.isEmpty()) {
                    CountryPickerRxResult.SearchStateChange(event.query, CountryPickerRxModel.CountryListState.Loading)
                } else {
                    CountryPickerRxResult.SearchStateChange(event.query, CountryPickerRxModel.CountryListState.Success(initialState))
                }
            },

        events.filter { it is CountryPickerEvent.SearchBoxChanged }
            .map { (it as CountryPickerEvent.SearchBoxChanged).query }
            .switchMap { query ->
                Observable.just(query)
                    .filter { it.isNotEmpty() }
                    .switchMap {
                        filterCountry(query)
                            .map { CountryPickerRxResult.SearchStateChange(query, it) }
                            .delaySubscription(200, TimeUnit.MILLISECONDS)
                            .startWithItem(CountryPickerRxResult.SearchStateChange(query, CountryPickerRxModel.CountryListState.Loading))
                    }
            },

        events.filter { it is CountryPickerEvent.ItemClicked }
            .map { it as CountryPickerEvent.ItemClicked }
            .doOnNext { saveRecentCountryUseCase(it.item.code) }
            .map { CountryPickerRxResult.SelectCountry(it.item) }

    ).scan(CountryPickerRxModel.empty()) { state, result ->
        when (result) {
            is CountryPickerRxResult.SelectCountry -> state.copy(selectedCountry = result.countryCodeModel)

            is CountryPickerRxResult.SearchStateChange -> {
                state.copy(searchBox = result.query, countryListState = result.state)
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
            }
}

fun SearchCountryCodeFilterUseCase.invokeRx(query: String): Observable<List<CountryPickerItem.Picker>> =
    Observable.just(query)
        .flatMapSingle {
            rxSingle { invoke(it) }
        }