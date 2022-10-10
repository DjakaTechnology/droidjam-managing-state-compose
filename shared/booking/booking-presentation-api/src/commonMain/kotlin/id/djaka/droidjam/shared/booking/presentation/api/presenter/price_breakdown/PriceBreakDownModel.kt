package id.djaka.droidjam.shared.booking.presentation.api.presenter.price_breakdown

import id.djaka.droidjam.shared.booking.presentation.api.model.PriceBreakDownDataModel

data class PriceBreakDownModel(
    val isExpanded: Boolean,
    val totalPriceState: TotalPriceState,
) {
    sealed class TotalPriceState {
        class Success(val totalPrice: PriceBreakDownDataModel) : TotalPriceState()
        object Loading : TotalPriceState()
    }

    val successState = totalPriceState as? TotalPriceState.Success

    val isSuccess = successState != null
}