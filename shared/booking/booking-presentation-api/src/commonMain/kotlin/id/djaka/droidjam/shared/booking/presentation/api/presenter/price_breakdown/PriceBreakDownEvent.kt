package id.djaka.droidjam.shared.booking.presentation.api.presenter.price_breakdown

sealed class PriceBreakDownEvent {
    class ToggleExpandCollapse(val isExpanded: Boolean) : PriceBreakDownEvent()
    class UpdateSpec(
        val addOnItemId: List<String>,
        val productId: String,
        val couponCode: String
    ) : PriceBreakDownEvent()
}