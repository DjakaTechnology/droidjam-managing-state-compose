package id.djaka.droidjam.shared.locale.app.presenter.country_picker

import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import id.djaka.driodjam.shared.core.molecule.MoleculePresenter
import id.djaka.droidjam.shared.locale.app.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryUseCases
import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerEvent
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_rx.CountryPickerRxModel
import id.djaka.droidjam.shared.locale.presentation.api.presenter.CountryPickerPresenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CountryPickerPresenterImpl(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
) : CountryPickerPresenter, MoleculePresenter<CountryPickerEvent, CountryPickerModel> {

    @Composable
    override fun present(event: Flow<CountryPickerEvent>): CountryPickerModel {
        var searchBox by remember { mutableStateOf("") }
        var selectedCountry by remember { mutableStateOf<CountryCodeModel?>(null) }
        val countryListState = presentCountryListState(searchBox)

        LaunchedEffect(event) {
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
            selectedCountry = selectedCountry,
            countryListState = countryListState,
        )
    }

    @Composable
    private fun presentCountryListState(query: String): CountryPickerModel.CountryListState {
        if (query.isEmpty()) {
            val initialState by remember { searchCountryUseCases.getSearchCountryCodeInitialStateFlow() }.collectAsState(listOf())
            return if (initialState.isEmpty()) {
                CountryPickerModel.CountryListState.Loading
            } else {
                CountryPickerModel.CountryListState.Success(initialState)
            }
        } else {
            var result: CountryPickerModel.CountryListState by remember { mutableStateOf(CountryPickerModel.CountryListState.Loading) }
            LaunchedEffect(query) {
                result = CountryPickerModel.CountryListState.Loading
                result = filterCountry(query)
            }
            return result
        }
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

private class CountryPickerPresenter(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
) : MoleculePresenter<CountryPickerEvent, CountryPickerRxModel> {

    @Composable
    override fun present(event: Flow<CountryPickerEvent>): CountryPickerRxModel {
        var searchBox by remember { mutableStateOf("") }
        var selectedCountry by remember { mutableStateOf<CountryCodeModel?>(null) }

        val initialStateList by remember { searchCountryUseCases.getSearchCountryCodeInitialStateFlow() }.collectAsState(listOf())

        val initialState = if (initialStateList.isEmpty()) CountryPickerRxModel.CountryListState.Loading
        else CountryPickerRxModel.CountryListState.Success(initialStateList)

        var filterCountry by remember { mutableStateOf<CountryPickerRxModel.CountryListState>(CountryPickerRxModel.CountryListState.Loading) }

        val currentState = if (searchBox.isEmpty()) initialState else filterCountry

        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(event) {
            event.collect {
                when (it) {
                    is CountryPickerEvent.ItemClicked -> {
                        selectedCountry = it.item
                        saveRecentCountryUseCase(it.item.code)
                        searchBox = ""
                    }

                    is CountryPickerEvent.SearchBoxChanged -> coroutineScope.launch {
                        searchBox = it.query

                        filterCountry = CountryPickerRxModel.CountryListState.Loading
                        filterCountry = filterCountry(it.query)
                    }
                }
            }
        }

        return CountryPickerRxModel(
            searchBox = searchBox,
            selectedCountry = selectedCountry,
            countryListState = currentState,
        )
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