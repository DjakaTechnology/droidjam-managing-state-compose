package id.djaka.droidjam.shared.booking.presentation.api.model.addon.item

data class AddonSelectorModel(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val isChecked: Boolean,
)