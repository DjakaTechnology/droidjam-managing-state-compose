package id.djaka.droidjam.common.domain

import id.djaka.droidjam.common.repository.AddonRepository
import id.djaka.droidjam.common.ui.booking.addon_widget.item.AddonSelectorModel

class GetAddOnSelectorUseCase(
    private val addonRepository: AddonRepository,
) {
    suspend operator fun invoke(): List<AddonSelectorModel> {
        return addonRepository.getList().map {
            AddonSelectorModel(
                id = it.id,
                title = it.title,
                description = it.description,
                price = it.price,
                isChecked = false
            )
        }
    }
}