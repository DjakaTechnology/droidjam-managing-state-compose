@file:OptIn(ExperimentalMaterial3Api::class)

package id.djaka.droidjam.common.ui.booking.addon_widget

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import id.djaka.droidjam.common.ui.theme.SpacerVertical
import id.djaka.droidjam.common.ui.theme.SpacingM

@Composable
fun AddonWidget(
    modifier: Modifier = Modifier,
    state: AddonPresenter.UIState,
    action: (AddonPresenter.Event) -> Unit
) {
    Column(modifier) {
        Text("Addon", style = MaterialTheme.typography.titleMedium)
        SpacerVertical(SpacingM)
        when (state.itemState) {
            AddonPresenter.UIState.ItemState.Loading -> CircularProgressIndicator()
            is AddonPresenter.UIState.ItemState.Success -> {
                AddonList(state.itemState, action)
            }
        }
    }
}

@Composable
private fun AddonList(itemState: AddonPresenter.UIState.ItemState.Success, action: (AddonPresenter.Event) -> Unit) {
    itemState.items.forEachIndexed { index, item ->
        val toggleCheckbox = { action(AddonPresenter.Event.CheckItem(index, !item.isChecked)) }
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