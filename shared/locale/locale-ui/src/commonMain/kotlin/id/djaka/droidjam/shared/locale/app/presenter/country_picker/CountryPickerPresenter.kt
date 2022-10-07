package id.djaka.droidjam.shared.locale.app.presenter.country_picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import id.djaka.droidjam.shared.core.framework.Presenter
import id.djaka.droidjam.shared.locale.app.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryUseCases
import id.djaka.droidjam.shared.locale.app.model.CountryCodeModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class CountryPickerPresenter(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
) : Presenter<CountryPickerEvent, CountryPickerModel> {

    private val initialStateFlow = searchCountryUseCases.getSearchCountryCodeInitialStateFlow()

    @Composable
    override fun present(event: Flow<CountryPickerEvent>): CountryPickerModel {
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

        return CountryPickerModel(
            searchBox = searchBox,
            countryListState = countryListState,
            selectedCountry = selectedCountry
        )
    }

    @Composable
    private fun presentCountryListState(query: String): CountryPickerModel.CountryListState {
        val initialState by initialStateFlow.collectAsState(listOf())
        var result: CountryPickerModel.CountryListState by remember { mutableStateOf(CountryPickerModel.CountryListState.Loading) }

        if (query.isEmpty()) {
            result = if (initialState.isEmpty()) {
                CountryPickerModel.CountryListState.Loading
            } else {
                CountryPickerModel.CountryListState.Success(initialState)
            }
        } else {
            LaunchedEffect(query) {
                result = CountryPickerModel.CountryListState.Loading
                println("Result: Reset Debounce")
                delay(200)
                println("Result: Fetching")
                result = filterCountry(query)
                println("Result updated")
            }
        }

        return result
    }

    private suspend fun filterCountry(query: String): CountryPickerModel.CountryListState {
        val filterResult = searchCountryUseCases.searchCountryCodeFilter(query)
        return if (filterResult.isEmpty()) {
            CountryPickerModel.CountryListState.Empty("Sorry, we can't found country with \"$query\"")
        } else {
            CountryPickerModel.CountryListState.Success(filterResult)
        }
    }
    
}