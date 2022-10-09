package id.djaka.droidjam.shared.booking.app.domain

import id.djaka.droidjam.shared.booking.app.repository.AddonRepository
import id.djaka.droidjam.shared.booking.presentation.api.model.addon.item.AddonSelectorModel

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