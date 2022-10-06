@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package id.djaka.droidjam.common.ui.country_picker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import id.djaka.droidjam.common.Platform
import id.djaka.droidjam.common.di.CoreDIManager
import id.djaka.droidjam.common.framework.rememberLaunchMoleculePresenter
import id.djaka.droidjam.common.getPlatform
import id.djaka.droidjam.common.model.CountryCodeModel
import id.djaka.droidjam.common.ui.country_picker.item.CountryPickerItem
import id.djaka.droidjam.common.ui.theme.*
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@Composable
fun CountryPickerScreen(
    state: CountryPickerPresenter.UIState,
    event: (CountryPickerPresenter.Event) -> Unit = {},
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
//            text = stringResource(id = R.string.country_code_search_title),
            text = "Search title",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        if (state.selectedCountry != null) {
            Text("Selected " + state.selectedCountry.name)
        }
        Spacer(modifier = Modifier.size(12.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            value = query,
            onValueChange = { event(CountryPickerPresenter.Event.SearchBoxChanged(it)) },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(0.25f),
//                    text = stringResource(id = R.string.country_code_placeholder),
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
            leadingIcon = {
//                Icon(
//                    modifier = Modifier.size(24.dp),
////                    painter = painterResource(R.drawable.ic_search),
//                    painter = painterResource("drawable/ic_search.xml"),
//                    contentDescription = "search",
//                )
            }
        )

        when (itemState) {
            CountryPickerPresenter.UIState.CountryListState.Loading -> {
                Box(Modifier.fillMaxWidth().padding(SpacingM), contentAlignment = Alignment.Center) {
                    LinearProgressIndicator(Modifier.fillMaxWidth())
                }
            }

            is CountryPickerPresenter.UIState.CountryListState.Empty -> {
                Box(Modifier.fillMaxWidth().padding(SpacingM)) {
                    Text(itemState.message)
                }
            }

            is CountryPickerPresenter.UIState.CountryListState.Success -> {
                CountryCodeList(itemState.countryCodes, keyboard, event)
            }
        }
    }
}

@Composable
private fun CountryCodeList(items: List<CountryPickerItem>, keyboard: SoftwareKeyboardController?, event: (CountryPickerPresenter.Event) -> Unit) {
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

                            event(CountryPickerPresenter.Event.ItemClicked(it))
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
    if (getPlatform() == Platform.DESKTOP) {
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

private fun countryPickerItem() = CountryPickerItem.Picker(
    CountryCodeModel(
        "Ascension Island",
        "AC",
        "\uD83C\uDDE6\uD83C\uDDE8 ",
        "U+1F1E6 U+1F1E8",
        ""
    )
)

//@Preview
//@Composable
//private fun ItemPreview() {
//    CoreTheme {
//        CountryItem(item = countryPickerItem())
//    }
//}
//
//@Preview
//@Composable
//fun DefaultPreview() {
//    val viewModel = remember {
//        CountryPickerPresenter.State(
//            searchBox = "",
//            countryListState = CountryPickerPresenter.State.CountryListState.Success(
//                listOf(
//                    countryPickerItem(),
//                    countryPickerItem(),
//                )
//            )
//        )
//    }
//
//    CoreTheme {
//        CountryPickerScreen(
//            viewModel,
//        )
//    }
//}
