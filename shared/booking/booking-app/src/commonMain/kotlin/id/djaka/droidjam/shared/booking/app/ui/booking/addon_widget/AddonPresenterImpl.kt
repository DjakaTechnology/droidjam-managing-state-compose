package id.djaka.droidjam.shared.booking.app.ui.booking.addon_widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import id.djaka.driodjam.shared.core.molecule.MoleculePresenter
import id.djaka.droidjam.shared.booking.app.domain.GetAddOnSelectorUseCase
import id.djaka.droidjam.shared.booking.presentation.api.model.addon.item.AddonSelectorModel
import id.djaka.droidjam.shared.booking.presentation.api.presenter.addon.AddonEvent
import id.djaka.droidjam.shared.booking.presentation.api.presenter.addon.AddonModel
import id.djaka.droidjam.shared.booking.presentation.api.presenter.addon.AddonPresenter
import kotlinx.coroutines.flow.Flow

class AddonPresenterImpl constructor(
    private val getAddOnSelectorUseCase: GetAddOnSelectorUseCase,
) : AddonPresenter, MoleculePresenter<AddonEvent, AddonModel> {
    @Composable
    override fun present(event: Flow<AddonEvent>): AddonModel {
        var itemState: AddonModel.ItemState by remember { mutableStateOf(AddonModel.ItemState.Loading) }
        val items = remember { mutableStateListOf<AddonSelectorModel>() }

        LaunchedEffect(Unit) {
            itemState = AddonModel.ItemState.Loading

            val newItems = getAddOnSelectorUseCase()
            items.clear()
            items.addAll(newItems)

            itemState = AddonModel.ItemState.Success(items)
        }

        LaunchedEffect(Unit) {
            event.collect {
                when (it) {
                    is AddonEvent.CheckItem -> {
                        items[it.index] = items[it.index].copy(isChecked = it.isChecked)
                    }
                }
            }
        }

        return AddonModel(
            itemState = itemState
        )
    }

}