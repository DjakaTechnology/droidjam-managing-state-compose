package id.djaka.droidjam.common.ui.country_picker

import androidx.compose.runtime.*
import id.djaka.droidjam.common.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.common.domain.SearchCountryUseCases
import id.djaka.droidjam.common.framework.MoleculePresenter
import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem
import id.djaka.droidjam.common.util.CollectEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class CountryPickerPresenter(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
) : MoleculePresenter<CountryPickerPresenter.Event, CountryPickerPresenter.UIState> {

    private val initialStateFlow = searchCountryUseCases.getSearchCountryCodeInitialStateFlow()

    @Composable
    override fun present(event: Flow<Event>): UIState {
        var searchBox by remember { mutableStateOf("") }
        var selectedCountry by remember { mutableStateOf<CountryCodeModel?>(null) }
        val countryListState = presentCountryListState(searchBox)

        LaunchedEffect(countryListState) {
            event.collect {
                when (it) {
                    is Event.ItemClicked -> {
                        selectedCountry = it.item
                        saveRecentCountryUseCase(it.item.code)
                        searchBox = ""
                    }

                    is Event.SearchBoxChanged -> searchBox = it.query
                }
            }
        }

        return UIState(
            searchBox = searchBox,
            countryListState = countryListState,
            selectedCountry = selectedCountry
        )
    }

    @Composable
    private fun presentCountryListState(query: String): UIState.CountryListState {
        val initialState by initialStateFlow.collectAsState(listOf())
        var result: UIState.CountryListState by remember { mutableStateOf(UIState.CountryListState.Loading) }

        if (query.isEmpty()) {
            result = if (initialState.isEmpty()) {
                UIState.CountryListState.Loading
            } else {
                UIState.CountryListState.Success(initialState)
            }
        } else {
            LaunchedEffect(query) {
                result = UIState.CountryListState.Loading
                println("Result: Reset Debounce")
                delay(200)
                println("Result: Fetching")
                result = filterCountry(query)
                println("Result updated")
            }
        }

        return result
    }

    private suspend fun filterCountry(query: String): UIState.CountryListState {
        val filterResult = searchCountryUseCases.searchCountryCodeFilter(query)
        return if (filterResult.isEmpty()) {
            UIState.CountryListState.Empty("Sorry, we can't found country with \"$query\"")
        } else {
            UIState.CountryListState.Success(filterResult)
        }
    }

    @Immutable
    data class UIState(
        val searchBox: String,
        val countryListState: CountryListState,
        val selectedCountry: CountryCodeModel? = null
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
                countryListState = UIState.CountryListState.Loading,
                selectedCountry = null,
            )
        }
    }

    @Immutable
    sealed class Event {
        class SearchBoxChanged(val query: String) : Event()
        class ItemClicked(val item: CountryCodeModel) : Event()
    }
}