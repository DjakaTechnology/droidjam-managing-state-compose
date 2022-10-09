@file:OptIn(ExperimentalMaterial3Api::class)

package id.djaka.droidjam.shared.booking.ui.addon

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import id.djaka.droidjam.shared.booking.presentation.api.presenter.addon.AddonEvent
import id.djaka.droidjam.shared.booking.presentation.api.presenter.addon.AddonModel
import id.djaka.droidjam.shared.core_ui.theme.SpacerVertical
import id.djaka.droidjam.shared.core_ui.theme.SpacingM

@Composable
fun AddonWidget(
    modifier: Modifier = Modifier,
    state: AddonModel,
    action: (AddonEvent) -> Unit
) {
    Column(modifier) {
        Text("Addon", style = MaterialTheme.typography.titleMedium)
        SpacerVertical(SpacingM)
        when (val itemState = state.itemState) {
            AddonModel.ItemState.Loading -> CircularProgressIndicator()
            is AddonModel.ItemState.Success -> {
                AddonList(itemState, action)
            }
        }
    }
}

@Composable
private fun AddonList(itemState: AddonModel.ItemState.Success, action: (AddonEvent) -> Unit) {
    itemState.items.forEachIndexed { index, item ->
        val toggleCheckbox = { action(AddonEvent.CheckItem(index, !item.isChecked)) }
        Card(
            onClick = toggleCheckbox
        ) {
            Row(Modifier.fillMaxWidth().padding(SpacingM), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(item.title, style = MaterialTheme.typography.titleMedium)
                    Text(item.description)

                    SpacerVertical(SpacingM)

                    Text("$${item.price}", color = MaterialTheme.colorScheme.primary)
                }
                Checkbox(
                    item.isChecked,
                    onCheckedChange = { toggleCheckbox() },
                )
            }
        }

        SpacerVertical(SpacingM)
    }
}