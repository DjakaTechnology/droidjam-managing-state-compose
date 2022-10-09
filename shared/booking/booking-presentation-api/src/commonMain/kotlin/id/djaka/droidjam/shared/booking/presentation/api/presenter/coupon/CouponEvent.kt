package id.djaka.droidjam.shared.booking.presentation.api.presenter.coupon

sealed class CouponEvent {
    class CouponTextChanged(val text: String) : CouponEvent()
    object ClearCoupon : CouponEvent()
}