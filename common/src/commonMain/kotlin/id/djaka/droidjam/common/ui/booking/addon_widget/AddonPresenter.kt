package id.djaka.droidjam.common.ui.booking.addon_widget

import androidx.compose.runtime.*
import id.djaka.droidjam.common.domain.GetAddOnSelectorUseCase
import id.djaka.droidjam.common.framework.Presenter
import id.djaka.droidjam.common.ui.booking.addon_widget.item.AddonSelectorModel
import kotlinx.coroutines.flow.Flow

class AddonPresenter constructor(
    private val getAddOnSelectorUseCase: GetAddOnSelectorUseCase,
) : Presenter<AddonPresenter.Event, AddonPresenter.Model> {
    @Composable
    override fun present(event: Flow<Event>): Model {
        var itemState: Model.ItemState by remember { mutableStateOf(Model.ItemState.Loading) }
        val items = remember { mutableStateListOf<AddonSelectorModel>() }

        LaunchedEffect(Unit) {
            itemState = Model.ItemState.Loading

            val newItems = getAddOnSelectorUseCase()
            items.clear()
            items.addAll(newItems)

            itemState = Model.ItemState.Success(items)
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

        return Model(
            itemState = itemState
        )
    }

    sealed class Event {
        class CheckItem(val index: Int, val isChecked: Boolean) : Event()
    }

    @Immutable
    class Model(
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