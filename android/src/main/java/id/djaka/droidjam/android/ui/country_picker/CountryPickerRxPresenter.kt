package id.djaka.droidjam.android.ui.country_picker

import id.djaka.droidjam.common.domain.SearchCountryUseCases
import id.djaka.droidjam.common.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.common.ui.country_picker.CountryPickerPresenter.Event
import id.djaka.droidjam.common.ui.country_picker.CountryPickerPresenter.UIState
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import kotlinx.coroutines.*
import kotlinx.coroutines.rx3.asObservable
import kotlinx.coroutines.rx3.rxObservable
import kotlinx.coroutines.rx3.rxSingle

class CountryPickerRxPresenter(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
) {

//    fun presentFlow(
//        coroutineScope: CoroutineScope,
//        event: Flow<Event>,
//    ): StateFlow<UIState> {
//        val query = MutableStateFlow("")
//        val selectedCountry = MutableStateFlow<CountryCodeModel?>(null)
//
//        coroutineScope.launch {
//            event.collect {
//                when (it) {
//                    is Event.ItemClicked -> {
//                        selectedCountry.value = it.item
//                        saveRecentCountryUseCase(it.item.code)
//                        query.value = ""
//                    }
//
//                    is Event.SearchBoxChanged -> query.value = it.query
//                }
//            }
//        }
//
//        return uiState(coroutineScope, query, selectedCountry)
//    }

//    private fun uiState(
//        coroutineScope: CoroutineScope,
//        queryFlow: StateFlow<String>,
//        selectedCountryFlow: StateFlow<CountryCodeModel?>
//    ) = combine(
//        queryFlow,
//        selectedCountryFlow,
//        presentCountryListStateFlow(queryFlow).onStart { emit(UIState.CountryListState.Loading) }
//    ) { query, selectedCountry, countryStateFlow ->
//        UIState(
//            searchBox = query,
//            countryListState = countryStateFlow,
//            selectedCountry = selectedCountry
//        )
//    }.stateIn(
//        coroutineScope, SharingStarted.WhileSubscribed(5000),
//        UIState.empty()
//    )

    fun presentRx(
        events: Observable<Event>
    ) = events.flatMap {
        when (it) {
            is Event.ItemClicked -> TODO()
            is Event.SearchBoxChanged -> {
                val query = Observable.just(it.query)
                Observable.merge(
                    query.map { Result.SearchBoxQueryChangeResult(it) },
                    countryListStateObservable(query)
                )
            }
        }
    }.scan(UIState.empty()) { state, result ->
        when (result) {
            is Result.SearchBoxQueryChangeResult -> state.copy(searchBox = result.query)
            is Result.SearchStateChangeResult -> state.copy(countryListState = result.state)
            else -> state
        }
    }

    private fun countryListStateObservable(queryObservable: Observable<String>) = Observable.combineLatest(
        queryObservable,
        searchCountryUseCases.getSearchCountryCodeInitialStateFlow().asObservable().startWith(Observable.just(listOf()))
    ) { query, initialState ->
        Pair(query, initialState)
    }.flatMap { (query, initialState) ->
        if (query.isEmpty()) {
            Observable.just(initialState).compose(transformInitialState())
        } else {
            Observable.just(query).compose(transformFilterCountry())
        }
    }.map { Result.SearchStateChangeResult(it) }

    private fun transformInitialState(): ObservableTransformer<List<CountryPickerItem>, UIState.CountryListState> = ObservableTransformer {
        it.map {
            if (it.isEmpty()) {
                UIState.CountryListState.Loading
            } else {
                UIState.CountryListState.Success(it)
            }
        }
    }

    private fun transformFilterCountry(): ObservableTransformer<String, UIState.CountryListState> = ObservableTransformer {
        it.flatMapSingle { query ->
            rxSingle<Pair<String, List<CountryPickerItem.Picker>>> {
                delay(2000) // Simulate Api call
                Pair(query, searchCountryUseCases.searchCountryCodeFilter(query))
            }
        }.map { (query, it) ->
            if (it.isEmpty()) {
                UIState.CountryListState.Empty("Sorry, we can't found country with \"$query\"")
            } else {
                UIState.CountryListState.Success(it)
            }
        }.startWith(Observable.just(UIState.CountryListState.Loading))
    }
}

sealed class Result {
    class SearchBoxQueryChangeResult(
        val query: String,
    ) : Result()

    class SearchStateChangeResult(
        val state: UIState.CountryListState
    ) : Result()
}