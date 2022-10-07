package id.djaka.droidjam.common.ui.country_picker.variant

import androidx.compose.runtime.Immutable
import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.ui.country_picker.CountryPickerPresenter
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem

@Immutable
data class CountryPickerRxModel(
    val searchBox: String,
    val countryListState: CountryListState,
    val selectedCountry: CountryCodeModel? = null,
    val initialList: CountryListState
) {
    @Immutable
    sealed class CountryListState {
        object Loading : CountryListState()
        data class Success(val countryCodes: List<CountryPickerItem>) : CountryListState()
        data class Empty(val message: String) : CountryListState()
    }

    companion object {
        fun empty() = CountryPickerRxModel(
            searchBox = "",
            countryListState = CountryListState.Loading,
            selectedCountry = null,
            initialList = CountryListState.Loading
        )
    }
}

fun CountryPickerRxModel.toGenericModel() = CountryPickerPresenter.Model(
    searchBox = searchBox,
    countryListState = countryListState.toGenericModel(),
    selectedCountry = selectedCountry
)

fun CountryPickerRxModel.CountryListState.toGenericModel() = when (this) {
    is CountryPickerRxModel.CountryListState.Empty -> CountryPickerPresenter.Model.CountryListState.Empty(message)
    CountryPickerRxModel.CountryListState.Loading -> CountryPickerPresenter.Model.CountryListState.Loading
    is CountryPickerRxModel.CountryListState.Success -> CountryPickerPresenter.Model.CountryListState.Success(countryCodes)
}