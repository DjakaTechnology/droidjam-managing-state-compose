package id.djaka.droidjam.android.ui.country_picker

import id.djaka.droidjam.common.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.common.domain.SearchCountryCodeFilterUseCase
import id.djaka.droidjam.common.domain.SearchCountryUseCases
import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.ui.country_picker.CountryPickerPresenter.Event
import id.djaka.droidjam.common.ui.country_picker.CountryPickerPresenter.UIState
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import kotlinx.coroutines.delay
import kotlinx.coroutines.rx3.asObservable
import kotlinx.coroutines.rx3.rxSingle
import java.util.concurrent.TimeUnit

class CountryPickerRxPresenter(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
) {

    fun presentRx(
        events: Observable<Event>
    ): Observable<UIState> = Observable.merge(
        searchCountryUseCases
            .getSearchCountryCodeInitialStateFlow()
            .asObservable()
            .map { Result.SearchStateChange(UIState.CountryListState.Success(it)) },

        events.filter { it is Event.SearchBoxChanged }
            .map { it as Event.SearchBoxChanged }
            .map { Result.SearchBoxQueryChange(it.query) },

        events.filter { it is Event.SearchBoxChanged }
            .debounce(200, TimeUnit.MILLISECONDS)
            .map { (it as Event.SearchBoxChanged).query }
            .compose(composeCountryListChangeResult()),

        events.filter { it is Event.ItemClicked }
            .map { it as Event.ItemClicked }
            .doOnNext { saveRecentCountryUseCase(it.item.code) }
            .map { Result.SelectCountry(it.item) }

    ).scan(UIState.empty()) { state, result ->
        when (result) {
            is Result.SearchBoxQueryChange -> state.copy(searchBox = result.query)
            is Result.SearchStateChange -> state.copy(countryListState = result.state)
            is Result.SelectCountry -> state.copy(selectedCountry = result.countryCodeModel)
            else -> state
        }
    }

    private fun composeCountryListChangeResult() = ObservableTransformer<String, Result.SearchStateChange> {
        it.switchMap { query ->
            if (query.isEmpty()) {
                searchCountryUseCases.getSearchCountryCodeInitialStateFlow().asObservable()
                    .startWith(Observable.just(listOf()))
                    .switchMap { initialState ->
                        Observable.just(initialState).compose(transformInitialState())
                    }
            } else {
                Observable.just(query).compose(transformFilterCountry())
            }
        }.map {
            Result.SearchStateChange(it)
        }.startWith(
            Observable.just(Result.SearchStateChange(UIState.CountryListState.Loading))
        )
    }

    private fun transformInitialState(): ObservableTransformer<List<CountryPickerItem>, UIState.CountryListState> = ObservableTransformer {
        it.map {
            if (it.isEmpty()) {
                UIState.CountryListState.Loading
            } else {
                UIState.CountryListState.Success(it)
            }
        }
    }

    private fun transformFilterCountry(): ObservableTransformer<String, UIState.CountryListState> = ObservableTransformer { queryObservable ->
        queryObservable.flatMap {
            Observable.zip(
                Observable.just(it),
                searchCountryUseCases.searchCountryCodeFilter.invokeRx(it)
            ) { query, result ->
                Pair(query, result)
            }
        }.map { (query, result) ->
            if (result.isEmpty()) {
                UIState.CountryListState.Empty("Sorry, we can't found country with \"$query\"")
            } else {
                UIState.CountryListState.Success(result)
            }
        }.startWith(Observable.just(UIState.CountryListState.Loading))
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
    }
}

fun SearchCountryCodeFilterUseCase.invokeRx(query: String): Observable<List<CountryPickerItem.Picker>> =
    Observable.just(query)
        .flatMapSingle {
            rxSingle {
                delay(2000) // Simulate Api call
                invoke(it)
            }
        }