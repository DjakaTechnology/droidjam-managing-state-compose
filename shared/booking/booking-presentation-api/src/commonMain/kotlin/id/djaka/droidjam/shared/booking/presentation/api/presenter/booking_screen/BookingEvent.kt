package id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_screen

import id.djaka.droidjam.shared.booking.presentation.api.presenter.price_breakdown.PriceBreakDownEvent

sealed class BookingEvent {
    data class AddonEvent(val event: id.djaka.droidjam.shared.booking.presentation.api.presenter.addon.AddonEvent) : BookingEvent()
    data class BookingInfoEvent(val event: id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_info.BookingInfoEvent) : BookingEvent()
    data class CouponEvent(val event: id.djaka.droidjam.shared.booking.presentation.api.presenter.coupon.CouponEvent) : BookingEvent()
    data class PriceBreakdownEvent(val event: PriceBreakDownEvent) : BookingEvent()
    data class Submit(val forceSubmit: Boolean = false) : BookingEvent()
    object DismissRequireUserActionDialog : BookingEvent()
    object DismissSuccessDialog : BookingEvent()
}