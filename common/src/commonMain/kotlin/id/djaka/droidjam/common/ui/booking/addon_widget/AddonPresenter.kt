package id.djaka.droidjam.common.ui.booking.addon_widget

import androidx.compose.runtime.*
import id.djaka.droidjam.common.domain.GetAddOnSelectorUseCase
import id.djaka.droidjam.common.framework.MoleculePresenter
import id.djaka.droidjam.common.ui.booking.addon_widget.item.AddonSelectorModel
import kotlinx.coroutines.flow.Flow

class AddonPresenter constructor(
    private val getAddOnSelectorUseCase: GetAddOnSelectorUseCase,
) : MoleculePresenter<AddonPresenter.Event, AddonPresenter.UIState> {
    @Composable
    override fun present(event: Flow<Event>): UIState {
        var itemState: UIState.ItemState by remember { mutableStateOf(UIState.ItemState.Loading) }
        val items = remember { mutableStateListOf<AddonSelectorModel>() }

        LaunchedEffect(Unit) {
            itemState = UIState.ItemState.Loading

            val newItems = getAddOnSelectorUseCase()
            items.clear()
            items.addAll(newItems)

            itemState = UIState.ItemState.Success(items)
        }

        LaunchedEffect(Unit) {
            event.collect {
                when (it) {
                    is Event.CheckItem -> {
                        items[it.index] = items[it.index].copy(isChecked = it.isChecked)
                    }
                }
            }
        }

        return UIState(
            itemState = itemState
        )
    }

    sealed class Event {
        class CheckItem(val index: Int, val isChecked: Boolean) : Event()
    }

    @Immutable
    class UIState(
        val itemState: ItemState
    ) {
        sealed class ItemState {
            object Loading : ItemState()
            data class Success(val items: List<AddonSelectorModel>) : ItemState()
        }

        val selectedAddon = if (itemState is ItemState.Success) {
            itemState.items.filter { it.isChecked }
        } else {
            listOf()
        }
    }
}