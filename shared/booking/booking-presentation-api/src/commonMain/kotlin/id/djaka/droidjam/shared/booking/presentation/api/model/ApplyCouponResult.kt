package id.djaka.droidjam.shared.booking.presentation.api.model

class ApplyCouponResult(
    val couponModel: CouponModel?,
    val message: String,
    val isSuccess: Boolean,
)