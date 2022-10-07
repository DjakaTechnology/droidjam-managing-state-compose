package id.djaka.droidjam.common.ui.country_picker

import androidx.compose.runtime.*
import id.djaka.droidjam.common.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.common.domain.SearchCountryUseCases
import id.djaka.droidjam.common.framework.Presenter
import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class CountryPickerPresenter(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
) : Presenter<CountryPickerEvent, CountryPickerPresenter.Model> {

    private val initialStateFlow = searchCountryUseCases.getSearchCountryCodeInitialStateFlow()

    @Composable
    override fun present(event: Flow<CountryPickerEvent>): Model {
        var searchBox by remember { mutableStateOf("") }
        var selectedCountry by remember { mutableStateOf<CountryCodeModel?>(null) }
        val countryListState = presentCountryListState(searchBox)

        LaunchedEffect(countryListState) {
            event.collect {
                when (it) {
                    is CountryPickerEvent.ItemClicked -> {
                        selectedCountry = it.item
                        saveRecentCountryUseCase(it.item.code)
                        searchBox = ""
                    }

                    is CountryPickerEvent.SearchBoxChanged -> searchBox = it.query
                }
            }
        }

        return Model(
            searchBox = searchBox,
            countryListState = countryListState,
            selectedCountry = selectedCountry
        )
    }

    @Composable
    private fun presentCountryListState(query: String): Model.CountryListState {
        val initialState by initialStateFlow.collectAsState(listOf())
        var result: Model.CountryListState by remember { mutableStateOf(Model.CountryListState.Loading) }

        if (query.isEmpty()) {
            result = if (initialState.isEmpty()) {
                Model.CountryListState.Loading
            } else {
                Model.CountryListState.Success(initialState)
            }
        } else {
            LaunchedEffect(query) {
                result = Model.CountryListState.Loading
                println("Result: Reset Debounce")
                delay(200)
                println("Result: Fetching")
                result = filterCountry(query)
                println("Result updated")
            }
        }

        return result
    }

    private suspend fun filterCountry(query: String): Model.CountryListState {
        val filterResult = searchCountryUseCases.searchCountryCodeFilter(query)
        return if (filterResult.isEmpty()) {
            Model.CountryListState.Empty("Sorry, we can't found country with \"$query\"")
        } else {
            Model.CountryListState.Success(filterResult)
        }
    }

    @Immutable
    data class Model(
        val searchBox: String,
        val countryListState: CountryListState,
        val selectedCountry: CountryCodeModel? = null
    ) {

        val selectedCountryDisplay = if (selectedCountry != null) "Selected ${selectedCountry.name}." else null
        @Immutable
        sealed class CountryListState {
            object Loading : CountryListState()
            data class Success(val countryCodes: List<CountryPickerItem>) : CountryListState()
            data class Empty(val message: String) : CountryListState()
        }

        companion object {
            fun empty() = Model(
                searchBox = "",
                countryListState = CountryListState.Loading,
                selectedCountry = null,
            )
        }
    }
}