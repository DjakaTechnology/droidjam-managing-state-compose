@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

package id.djaka.droidjam.shared.locale.ui.country_picker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import id.djaka.droidjam.shared.core.PlatformType
import id.djaka.droidjam.shared.core.getPlatform
import id.djaka.droidjam.shared.core_ui.theme.CardClickableShape
import id.djaka.droidjam.shared.core_ui.theme.SpacerVertical
import id.djaka.droidjam.shared.core_ui.theme.SpacingM
import id.djaka.droidjam.shared.core_ui.theme.SpacingML
import id.djaka.droidjam.shared.core_ui.theme.SpacingS
import id.djaka.droidjam.shared.core_ui.theme.appSurfaceColorAtElevation
import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerEvent
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerModel
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.item.CountryPickerItem
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import io.ktor.http.Url

@Composable
fun CountryPickerScreen(
    state: CountryPickerModel,
    event: (CountryPickerEvent) -> Unit = {},
) {
    val itemState = state.countryListState
    val query = state.searchBox
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = SpacingM, start = SpacingML, end = SpacingM)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Search title",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )

        state.selectedCountryDisplay?.let {
            Text(it)
        }

        Spacer(modifier = Modifier.size(12.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            value = query,
            onValueChange = { event(CountryPickerEvent.SearchBoxChanged(it)) },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(0.25f),
                    text = "Type anything",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            shape = CardClickableShape(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { focusManager.clearFocus() }
            ),
        )

        when (itemState) {
            CountryPickerModel.CountryListState.Loading -> {
                Box(Modifier.fillMaxWidth().padding(SpacingM), contentAlignment = Alignment.Center) {
                    LinearProgressIndicator(Modifier.fillMaxWidth())
                }
            }

            is CountryPickerModel.CountryListState.Empty -> {
                Box(Modifier.fillMaxWidth().padding(SpacingM)) {
                    Text(itemState.message)
                }
            }

            is CountryPickerModel.CountryListState.Success -> {
                CountryCodeList(itemState.countryCodes, keyboard, event)
            }
        }
    }
}

@Composable
private fun CountryCodeList(items: List<CountryPickerItem>, keyboard: SoftwareKeyboardController?, event: (CountryPickerEvent) -> Unit) {
    LazyColumn {
        itemsIndexed(items) { index, it ->
            SpacerVertical(SpacingS)

            when (it) {
                CountryPickerItem.Divider -> {
                    Divider(Modifier.padding(horizontal = SpacingM).height(0.5.dp))
                }

                is CountryPickerItem.Header -> {
                    Text(it.header, style = MaterialTheme.typography.titleSmall)
                }

                is CountryPickerItem.Picker -> {
                    CountryItem(
                        item = it,
                        onClick = {
                            keyboard?.hide()

                            event(CountryPickerEvent.ItemClicked(it))
                        })
                }
            }

            if (index == items.lastIndex) {
                SpacerVertical(SpacingM)
            }
        }
    }
}

@Composable
private fun CountryItem(
    modifier: Modifier = Modifier,
    item: CountryPickerItem.Picker,
    onClick: (CountryCodeModel) -> Unit = {}
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(0.dp),
        shape = CardClickableShape(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.appSurfaceColorAtElevation(5.dp)
        ),
        onClick = { onClick(item.item) }
    ) {
        Row(Modifier.padding(SpacingM)) {
            CountryFlag(item)

            Spacer(Modifier.width(8.dp))


            Text(
                modifier = Modifier.weight(1f),
                text = item.item.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                modifier = Modifier.alpha(0.5f),
                text = item.item.code,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun CountryFlag(item: CountryPickerItem.Picker) {
    if (getPlatform().platformType == PlatformType.DESKTOP || getPlatform().platformType == PlatformType.JS) {
        KamelImage(
            resource = lazyPainterResource(data = Url(item.item.image)),
            contentDescription = "flag",
            modifier = Modifier.size(24.dp),
            onLoading = {
                CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
            },
            onFailure = {
                Text("Failed to load: " + it.stackTraceToString())
            }
        )
    } else {
        Text(item.item.emoji)
    }
}

