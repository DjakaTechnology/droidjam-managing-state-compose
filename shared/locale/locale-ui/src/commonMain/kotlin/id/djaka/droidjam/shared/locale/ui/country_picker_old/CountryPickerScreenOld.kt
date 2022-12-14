package id.djaka.droidjam.shared.locale.ui.country_picker_old

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker_old.CountryPickerOldScreenViewModel
import id.djaka.droidjam.shared.locale.presentation.api.presenter.CountryPickerScreenAction

@Composable
fun CountryPickerScreenOld(state: CountryPickerOldScreenViewModel, action: CountryPickerScreenAction) {
    /*...*/

    val selectedCountry: String? by state.selectedCountryDisplay.collectAsState(null) // This is bad
    if (selectedCountry != null) {
        Text(selectedCountry ?: "")
    }

    val query by state.query.collectAsState()
    OutlinedTextField(
        value = query,
        onValueChange = { action.onSearchBoxChanged(it) }
    )

    val result by state.result.collectAsState()
    /*...*/
}