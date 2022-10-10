package id.djaka.droidjam.shared.locale.app.presenter.country_picker

import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import id.djaka.driodjam.shared.core.molecule.MoleculePresenter
import id.djaka.driodjam.shared.core.molecule.lib.RecompositionClock
import id.djaka.driodjam.shared.core.molecule.lib.launchMolecule
import id.djaka.droidjam.shared.locale.app.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryUseCases
import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerEvent
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerModel
import id.djaka.droidjam.shared.locale.presentation.api.presenter.CountryPickerPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class CountryPickerPresenterImpl(
    private val searchCountryUseCases: SearchCountryUseCases,
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase,
) : CountryPickerPresenter, MoleculePresenter<CountryPickerEvent, CountryPickerModel> {

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
                delay(200)

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