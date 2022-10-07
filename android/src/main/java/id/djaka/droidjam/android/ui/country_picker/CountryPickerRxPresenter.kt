package id.djaka.droidjam.android.ui.country_picker

import id.djaka.droidjam.shared.locale.app.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryCodeFilterUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryUseCases
import id.djaka.droidjam.shared.locale.app.presenter.country_picker.CountryPickerEvent
import id.djaka.droidjam.shared.locale.app.presenter.country_picker.item.CountryPickerItem
import id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxModel
import id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxResult
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
    ): Observable<id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxModel> = Observable.merge(
        searchCountryUseCases
            .getSearchCountryCodeInitialStateFlow()
            .asObservable()
            .map { id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxResult.InitialStateLoad(id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxModel.CountryListState.Success(it)) }
            .startWith(Observable.just(id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxResult.InitialStateLoad(id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxModel.CountryListState.Loading))),

        events.filter { it is CountryPickerEvent.SearchBoxChanged }
            .map { it as CountryPickerEvent.SearchBoxChanged }
            .switchMap {
                Observable.merge(
                    Observable.just(it.query)
                        .delay(200, TimeUnit.MILLISECONDS)
                        .filter { it.isNotEmpty() }
                        .compose(composeCountryListChangeResult()),
                    Observable.just(id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxResult.SearchBoxQueryChange(it.query)),
                )
            },

        events.filter { it is CountryPickerEvent.ItemClicked }
            .map { it as CountryPickerEvent.ItemClicked }
            .doOnNext { saveRecentCountryUseCase(it.item.code) }
            .map { id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxResult.SelectCountry(it.item) }

    ).scan(id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxModel.empty()) { state, result ->
        when (result) {
            is id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxResult.SelectCountry -> state.copy(selectedCountry = result.countryCodeModel)
            is id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxResult.SearchBoxQueryChange -> {
                if (result.query.isEmpty()) {
                    state.copy(searchBox = result.query, countryListState = state.initialList)
                } else {
                    state.copy(searchBox = result.query)
                }
            }

            is id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxResult.SearchStateChange -> {
                if (state.searchBox.isNotEmpty()) {
                    state.copy(countryListState = result.state)
                } else state
            }

            is id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxResult.InitialStateLoad -> {
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
            id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxResult.SearchStateChange(it)
        }.startWith(
            Observable.just(id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxResult.SearchStateChange(id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxModel.CountryListState.Loading))
        )
    }

    private fun transformFilterCountry(): ObservableTransformer<String, id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxModel.CountryListState> = ObservableTransformer { queryObservable ->
        queryObservable.flatMap {
            Observable.zip(
                Observable.just(it),
                searchCountryUseCases.searchCountryCodeFilter.invokeRx(it)
            ) { query, result ->
                Pair(query, result)
            }
        }.map { (query, result) ->
            if (result.isEmpty()) {
                id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxModel.CountryListState.Empty("Sorry, we can't found country with \"$query\"")
            } else {
                id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxModel.CountryListState.Success(result)
            }
        }.startWith(Observable.just(id.djaka.shared.local.app.presenter.country_picker.variant.CountryPickerRxModel.CountryListState.Loading))
    }
}

fun SearchCountryCodeFilterUseCase.invokeRx(query: String): Observable<List<CountryPickerItem.Picker>> =
    Observable.just(query)
        .flatMapSingle {
            rxSingle { invoke(it) }
        }