package id.djaka.droidjam.shared.booking.presentation.api.presenter.addon

import id.djaka.droidjam.shared.booking.presentation.api.model.addon.item.AddonSelectorModel

class AddonModel(
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