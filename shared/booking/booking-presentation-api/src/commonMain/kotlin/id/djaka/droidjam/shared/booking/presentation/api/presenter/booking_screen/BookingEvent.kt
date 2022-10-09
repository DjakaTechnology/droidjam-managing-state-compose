package id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_screen

import id.djaka.droidjam.shared.booking.presentation.api.presenter.addon.AddonEvent
import id.djaka.droidjam.shared.booking.presentation.api.presenter.booking_info.BookingInfoEvent
import id.djaka.droidjam.shared.booking.presentation.api.presenter.coupon.CouponEvent
import id.djaka.droidjam.shared.booking.presentation.api.presenter.price_breakdown.PriceBreakDownEvent

sealed class BookingEvent {
    data class SendAddonEvent(val event: AddonEvent) : BookingEvent()
    data class SendBookingInfoEvent(val event: BookingInfoEvent) : BookingEvent()
    data class SendCouponEvent(val event: CouponEvent) : BookingEvent()
    data class SendPriceBreakdownEvent(val event: PriceBreakDownEvent) : BookingEvent()
    data class Submit(val forceSubmit: Boolean = false) : BookingEvent()
    object DismissRequireUserActionDialog : BookingEvent()
    object DismissSuccessDialog : BookingEvent()
}