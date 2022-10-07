package id.djaka.droidjam.shared.locale.ui.country_picker_old

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun CountryPickerScreenOld(state: CountryPickerScreenState, action: CountryPickerScreenAction) {
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

    /*...*/
}